package com.lay.fastipc

import com.google.gson.Gson
import com.lay.fastipc.service.FastIPCService
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import com.lay.fastipc.client.FastIPCChannel
import com.lay.fastipc.model.REQUEST_TYPE

/**
 * author: qinlei
 * create by: 2023/1/24 22:07
 * description:
 */
class FastIPCInvocationHandler(
    val service: Class<out FastIPCService>,
    val serviceId: String?
) : InvocationHandler {

    private val gson = Gson()

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {

        //执行客户端发送方法请求
        val response = FastIPCChannel.getDefault()
            .send(
                REQUEST_TYPE.INVOKE_METHOD.ordinal,
                service,
                serviceId,
                method?.name ?: "",
                args
            )
        //拿到服务端返回的结果
        if (response != null && response.result ==200) {
            //反序列化得到结果
            return gson.fromJson(response.value, method?.returnType)
        }


        return null
    }

}