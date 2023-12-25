package com.xinzy.compose.wan.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.ui.widget.ArticleItem
import com.xinzy.compose.wan.ui.widget.EditText
import com.xinzy.compose.wan.ui.widget.ProgressDialog
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L

@OptIn(ExperimentalFoundationApi::class)
data class WechatTabConfig(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WechatTab(
    tab: MainTabs,
    vm: MainViewModel,
    modifier: Modifier = Modifier,
    config: WechatTabConfig
) {

    val viewState = vm.wechatViewState

    val pageState: PagerState = config.getPageState(count = viewState.data.size)
    var clickedTabIndex by remember { mutableIntStateOf(config.currentSelectedTabIndex.intValue) }

    LaunchedEffect(key1 = Unit) {
        vm.dispatch(MainEvent.LoadWechatChapter)
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
                    WechatItem(
                        vm = vm,
                        modifier = Modifier.fillMaxSize(),
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WechatItem(
    vm: MainViewModel,
    modifier: Modifier = Modifier,
    index: Int,
    chapter: Chapter,
    lazyListState: LazyListState
) {
    val wechatPagingItems = vm.getWechatPager(chapter.id).collectAsLazyPagingItems()
    var keyword by remember { mutableStateOf(vm.getWechatKeyword(chapter.id)) }

    L.d("isRefreshing=${wechatPagingItems.isRefreshing}")
    val keyboard = LocalSoftwareKeyboardController.current

    SwipeRefresh(
        modifier = modifier,
        isRefreshing = wechatPagingItems.isRefreshing,
        onRefresh = {
            wechatPagingItems.refresh()
        }
    ) {

        LazyColumn(
            state = lazyListState
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    EditText(
                        value = keyword,
                        onValueChange = {
                            keyword = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            L.d("keyboard=$keyboard")
                            keyboard?.hide()
                            if (keyword == vm.getWechatKeyword(chapter.id)) {
                                L.d("keyword is same and need not to refresh")
                            } else {
                                vm.dispatch(MainEvent.UpdateWechatKeyword(cid = chapter.id, keyword = keyword))
                                wechatPagingItems.refresh()
                            }
                        }),
                        placeholder = "搜索关键字",
                        leadingIcon = IconFont.Search,
                        trailingIcon = IconFont.Close,
                        onTrailingClick = {
                            keyword = ""
                            if (keyword == vm.getWechatKeyword(chapter.id)) {
                                L.d("keyword is same and need not to refresh")
                            } else {
                                vm.dispatch(MainEvent.UpdateWechatKeyword(cid = chapter.id, keyword = keyword))
                                wechatPagingItems.refresh()
                            }
                        },
                    )
                }
            }

            items(wechatPagingItems) { article ->
                ArticleItem(article = article!!)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }

            createRefreshItem(wechatPagingItems)

            createLoadingItem(wechatPagingItems)
        }
    }
}