package com.xinzy.compose.wan.util

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object L {

    private var isDebug = true
    private var tag = "Wan"

    private var logFile: File? = null

    fun initLogFile(context: Context) {
        val dir = context.getExternalFilesDir("log") ?: return
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, SimpleDateFormat("HH-mm-ss").format(Date()))
        logFile = file
    }

    private fun writeLog(msg: String) {
        logFile?.let {
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            it.appendText("$time $msg \n")
        }
    }

    fun setDebug(b: Boolean) {
        isDebug = b
    }

    fun setTag(t: String) {
        tag = t
    }

    fun i(content: String) {
        if (!isDebug) return
        val msg = generateTag() + content
        Log.i(tag, msg)
        writeLog(msg)
    }

    fun v(content: String) {
        if (!isDebug) return
        val msg = generateTag() + content
        Log.v(tag, msg)
        writeLog(msg)
    }

    fun d(content: String) {
        if (!isDebug) return
        val msg = generateTag() + content
        Log.d(tag, msg)
        writeLog(msg)
    }

    fun w(content: String, tr: Throwable? = null) {
        if (!isDebug) return
        val msg = generateTag() + content
        Log.w(tag, msg, tr)
        writeLog(msg)
    }

    fun e(content: String, tr: Throwable? = null) {
        if (!isDebug) return
        val msg = generateTag() + content
        Log.e(tag, msg, tr)
        writeLog(msg)
    }

    private fun generateTag(): String {
        val caller = Thread.currentThread().stackTrace[4]
        var callerClazzName = caller.className
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
        return "$callerClazzName.${caller.methodName}(${caller.fileName}:${caller.lineNumber}) "
    }
}