package com.lay.fastipc

import android.content.Context
import android.util.Log
import com.lay.fastipc.server.FastIPCRegistry
import com.lay.fastipc.service.FastIPCService
import java.lang.reflect.Proxy
import com.lay.fastipc.client.FastIPCChannel
import com.lay.fastipc.annatation.ServiceId
import com.lay.fastipc.model.REQUEST_TYPE

/**
 * author: qinlei
 * create by: 2023/1/23 20:56
 * description:
 */
object FastIPC {

    //==========================================

    /**
     * 服务端暴露的接口，用于注册服务使用
     */
    fun register(service: Class<*>) {
        FastIPCRegistry.instance.register(service)
    }


    //===========================================

    /**
     * 客户端与服务端建立连接
     * @param pkgName 客户端应用的包名
     * @param action 绑定服务需要的action
     * @param service 业务方需要使用的服务
     */
    fun connect(
        context: Context,
        pkgName: String,
        action: String = "",
        service: Class<out FastIPCService>
    ) {

        FastIPCChannel.getDefault().connect(context, pkgName, action, service)
    }


    fun <T> getInstanceWithName(
        service: Class<out FastIPCService>,
        classType: Class<T>,
        methodName: String,
        params: Array<Any>?
    ): T? {

        //获取serviceId
        val serviceId = classType.getAnnotation(ServiceId::class.java)

        val response = FastIPCChannel.getDefault()
            .send(REQUEST_TYPE.GET_INSTANCE.ordinal, service, serviceId.name, methodName, params)
        Log.e("TAG", "response $response")
        if (response != null && response.result == 200) {
            //请求成功，返回接口实例对象
            return Proxy.newProxyInstance(
                classType.classLoader,
                arrayOf(classType),
                FastIPCInvocationHandler(service,serviceId.name)
            ) as T
        }

        return null
    }
}