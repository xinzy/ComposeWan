package com.xinzy.compose.wan.ui.main

import com.xinzy.compose.wan.entity.Article

enum class CollectSource {
    Home, Wechat, Project,
    ;
}

sealed class MainEvent {
    object LoadBanner : MainEvent()

    object LoadChapter : MainEvent()

    object LoadNavigationChapter : MainEvent()

    object LoadWechatChapter : MainEvent()

    object LoadProjectChapter : MainEvent()

    data class UpdateWechatKeyword(
        val cid: Int,
        val keyword: String
    ) : MainEvent()

    data class Collect(
        val article: Article,
        val collect: Boolean,
        val source: CollectSource
    ) : MainEvent()
}
