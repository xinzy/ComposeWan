package com.xinzy.compose.wan.ui.main

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.theme.Typography
import com.xinzy.compose.wan.ui.web.WebViewActivity
import com.xinzy.compose.wan.ui.widget.IconFontButton
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.ProgressDialog
import com.xinzy.compose.wan.ui.widget.ShowToast
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L

@OptIn(ExperimentalFoundationApi::class)
data class ProjectTabConfig(
    val currentSelectedTabIndex: MutableIntState,
    var pageState: PagerState? = null,
    var pageCount: Int = 0,
    val itemListState: MutableMap<Int, LazyListState> = hashMapOf()
) {
    @Composable
    fun getPageState(count: Int): PagerState {
        return if (pageCount == count) {
            pageState ?: rememberPagerState { count }.also { pageState = it }
        } else {
            pageCount = count
            rememberPagerState { count }.also { pageState = it }
        }
    }

    @Composable
    fun getItemListState(index: Int): LazyListState {
        return itemListState[index] ?: rememberLazyListState().also { itemListState[index] = it }
    }
}

fun interface OnProjectCollectCallback {
    fun onArticleCollect(article: Article, collect: Boolean)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectTab(
    tab: MainTabs,
    vm: MainViewModel,
    context: Context,
    config: ProjectTabConfig,
    modifier: Modifier = Modifier
) {
    val viewState = vm.projectViewState
    val collectState = vm.projectCollectState

    val isLogin = User.me().isLogin
    val pageState: PagerState = config.getPageState(count = viewState.data.size)
    var clickedTabIndex by remember { mutableIntStateOf(config.currentSelectedTabIndex.intValue) }

    LaunchedEffect(key1 = Unit) {
        vm.dispatch(MainEvent.LoadProjectChapter)
    }

    LaunchedEffect(key1 = clickedTabIndex) {
        L.d("pageState.currentPage=${pageState.currentPage} clickedTabIndex=$clickedTabIndex")
        if (pageState.currentPage != clickedTabIndex) {
            pageState.animateScrollToPage(clickedTabIndex)
        }
    }

    LaunchedEffect(key1 = pageState.targetPage) {
        L.d("pageState.targetPage=${pageState.targetPage} tabIndex=${config.currentSelectedTabIndex.intValue}")
        if (pageState.targetPage != config.currentSelectedTabIndex.intValue) {
            config.currentSelectedTabIndex.intValue = pageState.targetPage
        }
    }

    collectState?.let {
        ShowToast(
            msg = it.message
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (viewState.data.isNotEmpty()) {
            Column {
                ScrollableTabRow(
                    selectedTabIndex = config.currentSelectedTabIndex.intValue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    edgePadding = 0.dp
                ) {
                    viewState.data.forEachIndexed { index, chapter ->
                        Tab(
                            selected = index == config.currentSelectedTabIndex.intValue,
                            onClick = {
                                L.d("click tab item $index")
                                config.currentSelectedTabIndex.intValue = index
                                clickedTabIndex = index
                            },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = chapter.name
                            )
                        }
                    }
                }

                HorizontalPager(
                    state = pageState,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    ProjectPageItem(
                        vm = vm,
                        context = context,
                        modifier = Modifier.fillMaxSize(),
                        isLogin = isLogin,
                        index = index,
                        chapter = viewState.data[index],
                        lazyListState = config.getItemListState(index = index)
                    )
                }
            }
        } else if (viewState.refreshing) {
            ProgressDialog()
        }
    }
}


@Composable
private fun ProjectPageItem(
    vm: MainViewModel,
    context: Context,
    modifier: Modifier = Modifier,
    isLogin: Boolean,
    index: Int,
    chapter: Chapter,
    lazyListState: LazyListState
) {
    val projectPagingItems = vm.getProjectPager(chapter.id).collectAsLazyPagingItems()

    SwipeRefresh(
        modifier = modifier,
        isRefreshing = projectPagingItems.isRefreshing,
        onRefresh = {
            projectPagingItems.refresh()
        }
    ) {

        LazyColumn(
            state = lazyListState
        ) {
            items(projectPagingItems) { article ->
                ProjectItem(
                    article = article!!,
                    showCollect = isLogin,
                    clickAction = {
                        WebViewActivity.start(context, article.link)
                    },
                    callback = { art, collect ->
                        vm.dispatch(MainEvent.Collect(art, collect, CollectSource.Project))
                    }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }

            createRefreshItem(projectPagingItems)

            createLoadingItem(projectPagingItems)
        }
    }
}

@Composable
private fun ProjectItem(
    article: Article,
    modifier: Modifier = Modifier,
    showCollect: Boolean,
    clickAction: (() -> Unit)? = null,
    callback: OnProjectCollectCallback? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { clickAction?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showCollect) {

            Box(
                modifier = Modifier
                    .width(18.dp)
                    .height(18.dp),
                contentAlignment = Alignment.Center
            ) {
                IconFontButton(
                    icon = if (article.collect) IconFont.Favor else IconFont.UnFavor,
                    onClick = {
                        callback?.onArticleCollect(article, !article.collect)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = if (article.collect) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = modifier.width(12.dp))
        }

        AsyncImage(
            model = article.cover,
            contentDescription = article.title,
            modifier = Modifier
                .height(144.dp)
                .width(108.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconFontText(
                    icon = if (article.hasAuthor) IconFont.Author else IconFont.SharedUser,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier
                        .width(72.dp)
                        .padding(start = 4.dp),
                    text = article.displayAuthor,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = article.getCategory(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = modifier.height(6.dp))

            Text(
                text = article.displayTitle,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Typography.titleMedium
            )

            Spacer(modifier = modifier.height(6.dp))

            Text(
                text = article.desc,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = Typography.bodyMedium
            )

            Spacer(modifier = modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.niceDate,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}