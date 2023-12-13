package com.xinzy.compose.wan.entity

data class ScoreRecord(
    val id: Int,
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)
