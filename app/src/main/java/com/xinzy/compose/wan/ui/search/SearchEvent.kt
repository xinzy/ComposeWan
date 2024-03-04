package com.xinzy.compose.wan.ui.search

sealed class SearchEvent {

    object LoadingHotkey : SearchEvent()

    data class Search(val keyword: String): SearchEvent()
}