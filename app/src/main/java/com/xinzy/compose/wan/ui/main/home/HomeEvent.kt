package com.xinzy.compose.wan.ui.main.home

sealed class HomeEvent {
    object Refresh : HomeEvent()

    object LoadMore: HomeEvent()
}
