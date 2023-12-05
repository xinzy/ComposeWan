package com.xinzy.compose.wan

import android.app.Application

class WanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        private lateinit var instance: WanApplication

        @JvmStatic
        fun getInstance(): WanApplication = instance
    }
}
