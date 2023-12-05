package com.xinzy.compose.wan.ui.main.chapter

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.theme.Typography
import com.xinzy.compose.wan.ui.widget.TitleBar
import com.xinzy.compose.wan.util.L

@Composable
fun ChapterTab(
    tab: MainTabs,
    modifier: Modifier = Modifier
) {
    remember {
        1
    }
    Column {
        TitleBar(title = tab.title)

        val lazyState = rememberLazyListState()

        LazyColumn(
            state = lazyState
        ) {

            items(30) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(0.5.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        }

        L.d("ChapterTab: firstIndex=${lazyState.firstVisibleItemIndex}, " +
                "offset=${lazyState.firstVisibleItemScrollOffset} isScrollInProgress=${lazyState.isScrollInProgress}," +
                "backward=${lazyState.canScrollBackward}, forward=${lazyState.canScrollForward}," +
                "layoutInfo=${lazyState.layoutInfo.str()}")
    }

//    NestedScrollConnection()
}

private fun LazyListLayoutInfo.str(): String =
    "LazyListLayoutInfo[" +
            "beforeContentPadding=${this.beforeContentPadding}, endContentPadding=${this.beforeContentPadding}," +
            "viewportStartOffset=${this.viewportStartOffset}, viewportEndOffset=${this.viewportEndOffset}" +
            "]"

@Preview
@Composable
fun ChapterTabPreview() {
    ChapterTab(tab = MainTabs.Chapter)
}