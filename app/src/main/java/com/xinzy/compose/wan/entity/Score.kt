package com.xinzy.compose.wan.entity

import com.google.gson.annotations.SerializedName

data class Score(
    val rank: Int = 0,
    @SerializedName("coinCount")
    val coin: Int = 0,
    val userId: Int = 0,
    val username: String = "",
    val level: Int,
    val nickname: String
)