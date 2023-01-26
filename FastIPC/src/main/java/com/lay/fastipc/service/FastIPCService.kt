package com.lay.fastipc.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.lay.fastipc.IFastIPCService
import com.lay.fastipc.model.Response
import com.lay.fastipc.model.Request
import com.lay.fastipc.model.Parameters
import com.lay.fastipc.model.REQUEST_TYPE
import com.lay.fastipc.server.FastIPCRegistry

/**
 * author: qinlei
 * create by: 2023/1/24 08:35
 * description:
 */
abstract class FastIPCService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return BINDERS
    }

    companion object BINDERS : IFastIPCService.Stub() {

        private val gson = Gson()

        override fun send(request: Request?): Response? {
            //获取服务对象id
            val serviceId = request?.serviceId
            val methodName = request?.methodName
            val params = request?.params
            // 反序列化拿到具体的参数类型
            val neededParams = parseParameters(params)
            val method = FastIPCRegistry.instance.findMethod(serviceId, methodName, neededParams)
            Log.e("TAG", "method $method")
            Log.e("TAG", "neededParams $neededParams")
            when (request?.type) {

                REQUEST_TYPE.GET_INSTANCE.ordinal -> {
                    //==========执行静态方法
                    try {
                        var instance: Any? = null
                        instance = if (neededParams == null || neededParams.isEmpty()) {
                            method?.invoke(null)
                        } else {
                            method?.invoke(null, neededParams)
                        }
                        if (instance == null) {
                            return Response("instance == null", -101)
                        }
                        //存储实例对象
                        FastIPCRegistry.instance.setServiceInstance(serviceId ?: "", instance)
                        return Response(null, 200)
                    } catch (e: Exception) {
                        return Response("${e.message}", -102)
                    }
                }
                REQUEST_TYPE.INVOKE_METHOD.ordinal -> {
                    //==============执行普通方法
                    val instance = FastIPCRegistry.instance.getServiceInstance(serviceId)
                    if (instance == null) {
                        return Response("instance == null ", -103)
                    }
                    //方法执行返回的结果
                    return try {

                        val result = if (neededParams == null || neededParams.isEmpty()) {
                            method?.invoke(instance)
                        } else {
                            method?.invoke(instance, neededParams)
                        }
                        Response(gson.toJson(result), 200)
                    } catch (e: Exception) {
                        Response("${e.message}", -104)
                    }

                }
            }

            return null
        }

        private fun parseParameters(params: Array<Parameters?>?): Array<Any?>? {
            if (params == null || params.isEmpty()) {
                return null
            }
            val objects = arrayOfNulls<Any>(params.size)
            params.forEachIndexed { index, parameters ->
                objects[index] =
                    gson.fromJson(parameters?.value, Class.forName(parameters?.className))
            }
            return objects
        }
    }

}