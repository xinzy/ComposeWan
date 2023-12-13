package com.xinzy.compose.wan.entity

data class Navigation(
    val name: String = "",
    val cid: Int = 0,
    val articles: List<Article> = listOf()
)