package com.xinzy.compose.wan.ui.main.home

import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner
import com.xinzy.compose.wan.http.HttpResult
import com.xinzy.compose.wan.http.WanApi
import com.xinzy.compose.wan.util.L

object HomeRepository {

    var isLoadEnd = false

    private var currentPage = 0

    suspend fun article(page: Int = -1): List<Article> {
        val p = if (page < 0) currentPage else page

        L.d("load article page=$p")

        return when (val result = WanApi.api().articleList(p)) {
            is HttpResult.Failure -> {
                listOf()
            }
            is HttpResult.Success -> {
                val data = result.data.data!!

                currentPage = data.page
                isLoadEnd = data.over
                data.getData()
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
}