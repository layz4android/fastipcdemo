package com.lay.fastipc.server

import android.util.Log
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import com.lay.fastipc.annatation.ServiceId

/**
 * author: qinlei
 * create by: 2023/1/23 20:57
 * description:
 */
internal class FastIPCRegistry {

    //=======================================
    /**用于存储 serviceId 对应的服务 class对象*/
    private val serviceMaps: ConcurrentHashMap<String, Class<*>> by lazy {
        ConcurrentHashMap()
    }

    /**用于存储 服务中全部的方法*/
    private val methodsMap: ConcurrentHashMap<Class<*>, ConcurrentHashMap<String, Method>> by lazy {
        ConcurrentHashMap()
    }

    /**用户保存service对象的实例*/
    private val serviceInstanceMap: ConcurrentHashMap<String, Any> by lazy {
        ConcurrentHashMap()
    }


    //=======================================

    /**
     * 服务端注册方法
     * @param service 服务class对象
     */
    fun register(service: Class<*>) {

        // 获取serviceId与服务一一对应
        val serviceIdAnnotation = service.getAnnotation(ServiceId::class.java)
            ?: throw IllegalArgumentException("只有标记@ServiceId的服务才能够被注册")
        //获取serviceId
        val name = serviceIdAnnotation.name
        serviceMaps[name] = service
        //temp array
        val methods: ConcurrentHashMap<String, Method> = ConcurrentHashMap()
        // 获取服务当中的全部方法
        for (method in service.declaredMethods) {

            //这里需要注意，因为方法中存在重载方法，所以不能把方法名当做key，需要加上参数
            val buffer = StringBuffer()
            buffer.append(method.name).append("(")
            val params = method.parameterTypes
            if (params.size > 0) {
                buffer.append(params[0].name)
            }
            for (index in 1 until params.size) {
                buffer.append(",").append(params[index].name)
            }
            buffer.append(")")
            //保存
            methods[buffer.toString()] = method
        }
        //存入方法表
        methodsMap[service] = methods

        val entrySet: Set<Map.Entry<Class<*>, ConcurrentHashMap<String, Method>>> =
            methodsMap.entries
        for (map in entrySet) {
            Log.e("TAG", "entrySet key ${map.key}")
            val mapValue = map.value.entries
            for (entry in mapValue) {
                Log.e("TAG", "mapValue key ${entry.key}")
                Log.e("TAG", "mapValue value ${entry.value}")
            }
        }

    }

    fun findMethod(serviceId: String?, methodName: String?, neededParams: Array<Any?>?): Method? {
        Log.e("TAG", "serviceMaps $serviceMaps")
        Log.e("TAG", "methodsMap $methodsMap")
        //获取服务
        val serviceClazz = serviceMaps[serviceId] ?: return null
        //获取方法集合
        val methods = methodsMap[serviceClazz] ?: return null
        return methods[rebuildParamsFunc(methodName, neededParams)]
    }

    private fun rebuildParamsFunc(methodName: String?, params: Array<Any?>?): String {

        val stringBuffer = StringBuffer()
        stringBuffer.append(methodName).append("(")

        if (params == null || params.isEmpty()) {
            stringBuffer.append(")")
            return stringBuffer.toString()
        }
        stringBuffer.append(params[0]?.javaClass?.name)
        for (index in 1 until params.size) {
            stringBuffer.append(",").append(params[index]?.javaClass?.name)
        }
        stringBuffer.append(")")
        return stringBuffer.toString()
    }

    fun setServiceInstance(serviceId: String, result: Any) {
        serviceInstanceMap[serviceId] = result
    }

    fun getServiceInstance(serviceId: String?): Any? {
        if (serviceId == null) return null
        return serviceInstanceMap[serviceId]
    }


    companion object {
        val instance by lazy { FastIPCRegistry() }
    }
}