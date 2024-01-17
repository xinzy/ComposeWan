package com.xinzy.compose.wan.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    private lateinit var toast: Toast

    fun init(context: Context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    fun show(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        L.d("show toast $msg")
        toast.setText(msg)
        toast.duration = duration
        toast.show()
    }

    fun cancel() {
        toast.cancel()
    }
}
