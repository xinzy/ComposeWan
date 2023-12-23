package com.xinzy.compose.wan.ui.main

sealed class MainEvent {
    object LoadBanner : MainEvent()

    object LoadChapter : MainEvent()

    object LoadNavigationChapter : MainEvent()

    object LoadWechatChapter : MainEvent()

    data class UpdateWechatKeyword(
        val cid: Int,
        val keyword: String
    ) : MainEvent()
}
