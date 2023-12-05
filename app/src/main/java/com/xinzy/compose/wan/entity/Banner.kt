package com.xinzy.compose.wan.entity

import androidx.annotation.Keep

@Keep
data class Banner(
    var id: Int = 0,
    var title: String = "",
    var imagePath: String = "",
    var url: String = "",
    var desc: String = "",
    var order: Int = 0,
    var isVisible: Int = 0,
    var type: Int = 0
)