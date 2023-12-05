package com.xinzy.compose.wan.entity

import androidx.annotation.Keep

@Keep
data class Tag(
    var name: String = "",
    var url: String? = null
)