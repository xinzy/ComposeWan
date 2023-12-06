package com.xinzy.compose.wan.ui.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.widget.ArticleItem
import com.xinzy.compose.wan.ui.widget.Banner
import com.xinzy.compose.wan.ui.widget.LoadingMoreLazyColumn
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.TitleBar
import com.xinzy.compose.wan.ui.widget.needLoadingMore
import com.xinzy.compose.wan.util.L

@Composable
fun HomeTab(
    tab: MainTabs,
    modifier: Modifier = Modifier,
    vm: HomeViewModel = viewModel()
) {
    var refreshing by remember {
        mutableStateOf(false)
    }

    val viewState = vm.viewState
    val isRefresh = viewState.isRefresh
    val isLoadingMore = viewState.isLoadingMore
    val isLoadEnd = viewState.isLoadEnd

    val articles = viewState.articles
    val banners = viewState.banners

    refreshing = isRefresh

    val lazyState = rememberLazyListState()

    Column(
        modifier = modifier
    ) {
        TitleBar(title = tab.title)

        SwipeRefresh(
            isRefreshing = refreshing,
            onRefresh = { vm.dispatch(HomeEvent.Refresh) }
        ) {

            LoadingMoreLazyColumn(
                state = lazyState,
                loadAction = {
                    if (!isRefresh && !isLoadingMore && !isLoadEnd) {
                        vm.dispatch(HomeEvent.LoadMore)
                    }
                }
            ) {
                // 显示Banner
                if (banners.isNotEmpty()) {

                    item {
                        Banner(
                            items = banners,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(867f / 482)
                        ) { item ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = rememberAsyncImagePainter(model = item.imagePath),
                                    contentDescription = item.title,
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }

                items(articles) {
                    ArticleItem(
                        modifier = Modifier.clickable {
                            println(it)
                        },
                        article = it
                    )

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun HomeTabPreview() {
    HomeTab(tab = MainTabs.Main)
}