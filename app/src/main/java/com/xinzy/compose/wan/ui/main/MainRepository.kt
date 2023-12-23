package com.xinzy.compose.wan.ui.main

import com.xinzy.compose.wan.entity.ApiResult
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.Navigation
import com.xinzy.compose.wan.entity.WanList
import com.xinzy.compose.wan.http.HttpResult
import com.xinzy.compose.wan.http.WanApi
import com.xinzy.compose.wan.util.L

object MainRepository {

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
}