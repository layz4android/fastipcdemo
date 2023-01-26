package com.lay.fastipcdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.lay.fastipc.FastIPC
import com.lay.fastipcdemo.binder.User
import com.lay.fastipcdemo.binder.UserManager

class UserService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //这里假设服务端配置数据
        UserManager.getDefault().setUserInfo(User("ming",25))
        //注册
        FastIPC.register(UserManager::class.java)

        return super.onStartCommand(intent, flags, startId)
    }
}