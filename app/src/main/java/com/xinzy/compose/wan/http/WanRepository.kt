package com.xinzy.compose.wan.http

import com.xinzy.compose.wan.entity.ApiResult
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.Navigation
import com.xinzy.compose.wan.entity.Rank
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.entity.WanList
import com.xinzy.compose.wan.util.L

object WanRepository {

    /////////////////////////////////////////////////////////////////
    // 首页
    /////////////////////////////////////////////////////////////////

    suspend fun homeArticle(page: Int): ApiResult<WanList<Article>> {
        L.d("load article page=$page")

        return when (val result = WanApi.api().articleList(page)) {
            is HttpResult.Failure -> {
                ApiResult(result.code, result.msg)
            }
            is HttpResult.Success -> {
                result.data
            }
        }
    }

    suspend fun top(): List<Article> {
        return when (val result = WanApi.api().topArticle()) {
            is HttpResult.Failure -> {
                listOf()
            }
            is HttpResult.Success -> {
                result.data.data!!
            }
        }
    }

    suspend fun banner(): List<Banner> {
        return when (val result = WanApi.api().banner()) {
            is HttpResult.Failure -> {
                listOf()
            }
            is HttpResult.Success -> {
                result.data.data!!
            }
        }
    }

    suspend fun chapters(): List<Chapter> {
        return when (val result = WanApi.api().chapter()) {
            is HttpResult.Failure -> listOf()
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data!!
                } else {
                    listOf()
                }
            }
        }
    }

    suspend fun navigation(): List<Navigation> {
        return when (val result = WanApi.api().navigation()) {
            is HttpResult.Failure -> listOf()
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data!!
                } else {
                    listOf()
                }
            }
        }
    }

    suspend fun wechat(): List<Chapter> {
        return when (val result = WanApi.api().weixin()) {
            is HttpResult.Failure -> listOf()
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data!!
                } else {
                    listOf()
                }
            }
        }
    }

    suspend fun wechatArticle(cid: Int, page: Int, keyword: String): ApiResult<WanList<Article>> {
        return when (val result = WanApi.api().wechatArticleList(page, cid, keyword)) {
            is HttpResult.Failure -> {
                ApiResult(result.code, result.msg)
            }
            is HttpResult.Success -> {
                result.data
            }
        }
    }

    suspend fun project(): List<Chapter> {
        return when (val result = WanApi.api().project()) {
            is HttpResult.Failure -> listOf()
            is HttpResult.Success -> {
                if (result.data.isSuccess) {
                    result.data.data!!
                } else {
                    listOf()
                }
            }
        }
    }

    suspend fun projectArticle(cid: Int, page: Int): ApiResult<WanList<Article>> {
        return when (val result = WanApi.api().projectArticleList(page, cid)) {
            is HttpResult.Failure -> {
                ApiResult(result.code, result.msg)
            }
            is HttpResult.Success -> {
                result.data
            }
        }
    }

    /////////////////////////////////////////////////////////////////
    // 用户相关
    /////////////////////////////////////////////////////////////////

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

    suspend fun rank(page: Int): ApiResult<WanList<Rank>> {
        return when (val result = WanApi.api().rank(page)) {
            is HttpResult.Failure -> {
                ApiResult(result.code, result.msg)
            }
            is HttpResult.Success -> {
                result.data
            }
        }
    }
}