package com.lay.clinet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.lay.fastipc.FastIPC
import com.lay.fastipc.service.FastIPCService01
import com.lay.fastipcdemo.binder.IUserManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //连接
        FastIPC.connect(
            this,
            "com.lay.fastipcdemo",
            "android.intent.action.GET_USER_INFO",
            FastIPCService01::class.java
        )
        findViewById<TextView>(R.id.tv_get).setOnClickListener {
            val userManager = FastIPC.getInstanceWithName(
                FastIPCService01::class.java,
                IUserManager::class.java,
                "getDefault", null
            )
            Log.e("TAG", "get user info ${userManager?.getUserInfo()}")
        }
    }
}