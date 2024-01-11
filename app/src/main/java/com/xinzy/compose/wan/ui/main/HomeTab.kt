package com.xinzy.compose.wan.ui.main

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.web.WebViewActivity
import com.xinzy.compose.wan.ui.widget.ArticleItem
import com.xinzy.compose.wan.ui.widget.Banner
import com.xinzy.compose.wan.ui.widget.BannerContentType
import com.xinzy.compose.wan.ui.widget.ShowToast
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
    context: Context,
    modifier: Modifier = Modifier
) {
    val homePagingItems = vm.homeArticle.collectAsLazyPagingItems()
    val viewState = vm.homeViewState
    val banners = viewState.banners

    val collectState = vm.homeCollectState

    val isLogin = User.me().isLogin

    collectState?.let {
        ShowToast(
            msg = it.message,
            context = context
        )
    }

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

                    item(
                        contentType = BannerContentType
                    ) {
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
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = item.imagePath,
                                    contentDescription = item.title,
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }

                items(
                    items = homePagingItems,
                    key = { item -> item.id }
                ) { article ->
                    ArticleItem(
                        article = article!!,
                        clickAction = {
                            WebViewActivity.start(context, article.link)
                        },
                        showCollect = isLogin
                    ) { art, collect ->
                        vm.dispatch(MainEvent.Collect(art, collect, CollectSource.Home))
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
