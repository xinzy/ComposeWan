package com.xinzy.compose.wan.entity

data class Navigation(
    var name: String = "",
    var cid: Int = 0,
    var articles: List<Article> = listOf()
) {}