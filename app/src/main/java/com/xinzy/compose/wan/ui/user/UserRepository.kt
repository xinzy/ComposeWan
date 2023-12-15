package com.xinzy.compose.wan.ui.user

import com.xinzy.compose.wan.entity.ApiResult
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.entity.WanList
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

    suspend fun register(username: String, password: String, confirm: String): Pair<User?, String> =
        when (val result = WanApi.api().register(username, password, confirm)) {
            is HttpResult.Failure -> {
                null to result.msg
            }
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data to "注册成功"
                } else {
                    null to result.data.message
                }
            }
        }

    suspend fun logout() {
        WanApi.api().logout()
    }

    suspend fun score(): Score? {
        return when (val result = WanApi.api().coin()) {
            is HttpResult.Failure -> {
                null
            }
            is HttpResult.Success -> {
                result.data.data
            }
        }
    }

    suspend fun scoreList(page: Int): ApiResult<WanList<ScoreRecord>> {
        return when (val result = WanApi.api().coinList(page)) {
            is HttpResult.Failure -> {
                ApiResult(result.code, result.msg)
            }
            is HttpResult.Success -> {
                result.data
            }
        }
    }
}