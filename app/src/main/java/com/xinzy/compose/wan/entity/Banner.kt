package com.xinzy.compose.wan.entity

import androidx.annotation.Keep

@Keep
data class Banner(
    val id: Int = 0,
    val title: String = "",
    val imagePath: String = "",
    val url: String = "",
    val desc: String = "",
    val order: Int = 0,
    val isVisible: Int = 0,
    val type: Int = 0
)