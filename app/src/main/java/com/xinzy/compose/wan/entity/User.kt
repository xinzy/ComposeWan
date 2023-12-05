package com.xinzy.compose.wan.entity

import android.content.Context
import androidx.core.content.edit

data class User(
    var id: Int = 0,
    var username: String = "",
    var nickname: String = "",
    var icon: String = "",
    var type: Int = 0,
    var admin: Boolean = false,
    var token: String = ""
) {
    companion object {
        const val SP_USER = "user"
        private val sInstance: User by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { User() }

        @JvmStatic
        fun me() = sInstance
    }

    fun save(context: Context) {
        context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE).edit {
            putInt("id", id).putString("username", username).putString("nickname", nickname).putString("icon", icon)
                .putInt("type", type).putString("token", token).putBoolean("admin", admin)
        }
    }

    fun load(context: Context) {
        val sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE)
        id = sp.getInt("id", 0)
        username = sp.getString("username", "") ?: ""
        nickname = sp.getString("nickname", "") ?: ""
        icon = sp.getString("icon", "") ?: ""
        type = sp.getInt("type", 0)
        admin = sp.getBoolean("admin", false)
        token = sp.getString("token", "") ?: ""
    }

    fun copy(user: User): User {
        id = user.id
        username = user.username
        nickname = user.nickname
        icon = user.icon
        type = user.type
        admin = user.admin
        token = user.token

        return this
    }

    fun logout(context: Context) {
        id = 0
        username = ""
        nickname = ""
        icon = ""
        type = 0
        admin = false
        token = ""
        context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE).edit { clear() }
    }

    fun isLogin() = id > 0
}