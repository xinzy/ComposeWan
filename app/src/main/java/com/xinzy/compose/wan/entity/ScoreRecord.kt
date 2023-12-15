package com.xinzy.compose.wan.entity

data class ScoreRecord(
    val id: Int = 0,
    val coinCount: Int = 0,
    val date: Long = 0,
    val desc: String = "",
    val reason: String = "",
    val type: Int = 0,
    val userId: Int = 0,
    val userName: String = ""
)
