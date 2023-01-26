package com.lay.fastipc.annatation

import java.lang.annotation.ElementType

/**
 * author: qinlei
 * create by: 2023/1/23 21:13
 * description:
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceId(
    val name: String
)
