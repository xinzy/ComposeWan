package com.xinzy.compose.wan.ui.search

import com.xinzy.compose.wan.entity.Hotkey

data class SearchState(
    var inSearching: Boolean = false,
    var hotkeys: List<Hotkey> = listOf(),
    var keyword: String = ""
)