package com.xinzy.compose.wan.ui.chapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.xinzy.compose.wan.ui.theme.ComposeWanTheme

class ChapterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chapterId = intent.getIntExtra("chapterId", 0)
        val chapterName = intent.getStringExtra("chapterName") ?: ""

        setContent {
            ComposeWanTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ChapterScreen(
                        chapterId = chapterId,
                        chapterName = chapterName
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context, chapterId: Int, chapterName: String) {
            val intent = Intent(context, ChapterActivity::class.java).also {
                it.putExtra("chapterId", chapterId)
                it.putExtra("chapterName", chapterName)
            }
            context.startActivity(intent)
        }
    }
}
