package com.xinzy.compose.wan.entity

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import com.xinzy.compose.wan.WanApplication

data class User(
    var id: Int = 0,
    var username: String = "",
    var nickname: String = "",
    var icon: String = "",
    var type: Int = 0,
    var admin: Boolean = false,
    var token: String = ""
) {
    var loginState by mutableStateOf(false)

    fun save() {
        sp.edit {
            this.putInt("id", id)
                .putString("username", username)
                .putString("nickname", nickname)
                .putString("icon", icon)
                .putInt("type", type)
                .putString("token", token)
                .putBoolean("admin", admin)
        }
    }

    fun load() {
        id = sp.getInt("id", 0)
        username = sp.getString("username", "") ?: ""
        nickname = sp.getString("nickname", "") ?: ""
        icon = sp.getString("icon", "") ?: ""
        type = sp.getInt("type", 0)
        admin = sp.getBoolean("admin", false)
        token = sp.getString("token", "") ?: ""

        loginState = isLogin
    }

    fun login(user: User): User {
        id = user.id
        username = user.username
        nickname = user.nickname
        icon = user.icon
        type = user.type
        admin = user.admin
        token = user.token

        loginState = isLogin
        return this
    }

    fun logout() {
        id = 0
        username = ""
        nickname = ""
        icon = ""
        type = 0
        admin = false
        token = ""
        sp.edit {
            clear()
        }

        loginState = isLogin
    }

    val isLogin get() = id > 0

    companion object {
        private const val SP_USER = "user"
        private val instance: User by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { User() }

        private val sp: SharedPreferences
            get() = WanApplication.getInstance().getSharedPreferences(SP_USER, Context.MODE_PRIVATE)

        @JvmStatic
        fun me() = instance
    }
}