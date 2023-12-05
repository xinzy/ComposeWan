package com.xinzy.compose.wan.entity

import com.google.gson.annotations.SerializedName

data class Coin(
    var rank: Int = 0,
    @SerializedName("coinCount")
    var coin: Int = 0,
    var userId: Int = 0,
    var username: String = ""
) {}