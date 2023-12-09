package com.xinzy.compose.wan

import android.app.Application
import com.xinzy.compose.wan.entity.User

class WanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        User.me().load(this)
        instance = this
    }

    companion object {
        private lateinit var instance: WanApplication

        @JvmStatic
        fun getInstance(): WanApplication = instance
    }
}
