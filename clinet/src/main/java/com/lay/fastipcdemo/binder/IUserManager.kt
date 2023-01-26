package com.lay.fastipcdemo.binder

import com.lay.fastipc.annatation.ServiceId

/**
 * author: qinlei
 * create by: 2023/1/26 20:23
 * description:
 */
@ServiceId("UserService")
interface IUserManager {

    fun setUserInfo(user: User)
    fun getUserInfo(): User
}