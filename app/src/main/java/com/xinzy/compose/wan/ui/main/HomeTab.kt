package com.xinzy.compose.wan.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.xinzy.compose.wan.ui.widget.ArticleItem
import com.xinzy.compose.wan.ui.widget.Banner
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.L

data class HomeTabConfig(
    val lazyListState: LazyListState
)

@Composable
fun HomeTab(
    tab: MainTabs,
    vm: MainViewModel,
    config: HomeTabConfig,
    modifier: Modifier = Modifier
) {
    val homePagingItems = vm.homeArticle.collectAsLazyPagingItems()
    val viewState = vm.homeViewState
    val banners = viewState.banners

    LaunchedEffect(key1 = Unit) {
        vm.dispatch(MainEvent.LoadBanner)
    }

    Column(
        modifier = modifier
    ) {
        SwipeRefresh(
            isRefreshing = homePagingItems.isRefreshing,
            onRefresh = {
                L.d("home page refresh")
                homePagingItems.refresh()
            }
        ) {

            LazyColumn(
                state = config.lazyListState
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

                items(homePagingItems) { article ->
                    ArticleItem(
                        modifier = Modifier.clickable {
                            println(article)
                        },
                        article = article!!
                    ) { art, collect ->

                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                createRefreshItem(homePagingItems)

                createLoadingItem(homePagingItems)
            }
        }
    }
}


@Preview
@Composable
fun HomeTabPreview() {
    HomeTab(
        tab = MainTabs.Main,
        vm = viewModel(),
        config = HomeTabConfig(lazyListState = rememberLazyListState())
    )
}