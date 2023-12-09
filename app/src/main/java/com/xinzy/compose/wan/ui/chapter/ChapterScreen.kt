package com.xinzy.compose.wan.ui.chapter

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChapterScreen(
    chapterId: Int,
    chapterName: String,
    modifier: Modifier = Modifier
) {

    ModalNavigationDrawer(drawerContent = {
        Text(text = "123123123123123123")
    }) {
        Text(text = "Content XXXXXX")
    }
}