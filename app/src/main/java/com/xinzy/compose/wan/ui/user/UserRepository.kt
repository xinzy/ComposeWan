package com.xinzy.compose.wan.ui.user

import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.http.HttpResult
import com.xinzy.compose.wan.http.WanApi

object UserRepository {

    suspend fun login(username: String, password: String): Pair<User?, String> {
        return when (val result = WanApi.api().login(username, password)) {
            is HttpResult.Failure -> {
                null to result.msg
            }
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data to "登录成功"
                } else {
                    null to result.data.message
                }
            }
        }
    }

    suspend fun register(username: String, password: String, confirm: String): User? =
        when (val result = WanApi.api().register(username, password, confirm)) {
            is HttpResult.Failure -> null
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data
                } else {
                    null
                }
            }
        }
}