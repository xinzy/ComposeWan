package com.xinzy.compose.wan.ui.main

import com.xinzy.compose.wan.entity.Banner

data class HomeState(
    val isLoaded: Boolean = false,
    val banners: List<Banner> = emptyList(),
)

data class SingleState<T>(
    val isLoaded: Boolean = false,
    val refreshing: Boolean = false,
    val data: T
)
