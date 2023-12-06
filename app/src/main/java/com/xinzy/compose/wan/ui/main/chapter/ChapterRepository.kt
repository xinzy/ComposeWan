package com.xinzy.compose.wan.ui.main.chapter

import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.http.HttpResult
import com.xinzy.compose.wan.http.WanApi

object ChapterRepository {

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
}