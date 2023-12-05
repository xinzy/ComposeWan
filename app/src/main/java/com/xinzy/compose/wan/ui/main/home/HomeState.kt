package com.xinzy.compose.wan.ui.main.home

import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner

data class HomeState(
    val isRefresh: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoadEnd: Boolean = false,
    val banners: List<Banner> = emptyList(),
    val articles: List<Article> = listOf(),
)
