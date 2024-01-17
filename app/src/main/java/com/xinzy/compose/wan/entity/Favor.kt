package com.xinzy.compose.wan.entity

data class Favor(
    val id: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val link: String,
    val niceDate: String,
    val origin: String,
    val title: String,
    val originId: Int?,
    val publishTime: Long,
    val userId: Int,
    val visible: Int,
    val zan: Int,
)
