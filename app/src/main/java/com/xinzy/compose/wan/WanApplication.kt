package com.xinzy.compose.wan

import android.app.Application
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.util.ToastUtil

class WanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
        ToastUtil.init(this)
        User.me().load()
    }

    companion object {
        private lateinit var instance: WanApplication

        @JvmStatic
        fun getInstance(): WanApplication = instance
    }
}
