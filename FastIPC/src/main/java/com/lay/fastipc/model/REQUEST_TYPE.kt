package com.lay.fastipc.model

/**
 * author: qinlei
 * create by: 2023/1/24 22:33
 * description:
 */
enum class REQUEST_TYPE(val des: String) {
    GET_INSTANCE("getInstance"), INVOKE_METHOD("invokeMethod")
}