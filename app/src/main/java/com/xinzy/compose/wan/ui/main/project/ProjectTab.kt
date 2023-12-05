package com.xinzy.compose.wan.ui.main.project

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.SwipeRefreshState
import com.xinzy.compose.wan.ui.widget.TitleBar
import com.xinzy.compose.wan.ui.widget.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun ProjectTab(
    tab: MainTabs,
    modifier: Modifier = Modifier
) {
    Column {
        TitleBar(title = tab.title)

        var list by remember {
            mutableStateOf(List(20) { "Item $it" })
        }
        var refreshing by remember {
            mutableStateOf(false)
        }
        var loadingMore by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = refreshing, key2 = loadingMore) {
            Log.d("YSZ", "ProjectTab: refreshing=$refreshing, loadingMore=$loadingMore")
            if (refreshing) {
                delay(2000)
                list = List(20) { "Refresh $it" }
                refreshing = false
            }
            if (loadingMore) {
                delay(2000)
                val size = list.size
                list += List(10) { "Loading More ${size + it}" }
                loadingMore = false
            }
        }

        SwipeRefresh(isRefreshing = refreshing, onRefresh = { refreshing = true }) {

            val lazyState = rememberLazyListState()

            LazyColumn(
                state = lazyState
            ) {
                items(
                    items = list
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it,
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

            if (!lazyState.isScrollInProgress && !lazyState.canScrollForward) {
                loadingMore = true
            }
        }
    }
}


@Preview
@Composable
fun ProjectTabPreview() {
    ProjectTab(tab = MainTabs.Project)
}