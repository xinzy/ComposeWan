package com.xinzy.compose.wan.ui.main.chapter

import com.xinzy.compose.wan.entity.Chapter

data class ChapterState(
    val refreshing: Boolean = false,
    val chapters: List<Chapter> = emptyList()
)
