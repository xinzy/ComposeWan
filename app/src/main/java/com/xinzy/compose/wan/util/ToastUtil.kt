package com.xinzy.compose.wan.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    private var toast: Toast? = null

    fun init(context: Context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    fun show(msg: String, duration: Int) {
        toast?.let {
            it.setText(msg)
            it.duration = duration
            it.show()
        }
    }
}
