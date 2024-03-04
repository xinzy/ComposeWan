package com.xinzy.compose.wan.ui.search

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.ui.web.WebViewActivity
import com.xinzy.compose.wan.ui.widget.ArticleItem
import com.xinzy.compose.wan.ui.widget.EditText
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    context: Context,
    modifier: Modifier = Modifier,
    vm: SearchViewModel = viewModel()
) {

    fun doSearch(keyword: String, lazyPagingItems: LazyPagingItems<Article>) {
        vm.dispatch(SearchEvent.Search(keyword))
        lazyPagingItems.refresh()
    }

    val searchState = vm.searchState
    val pagingItems = vm.searchPaging.collectAsLazyPagingItems()

    var searchText: String by remember { mutableStateOf(searchState.keyword) }

    val searchResultLazyListState = remember { LazyListState() }

    LaunchedEffect(key1 = Unit) {
        vm.dispatch(SearchEvent.LoadingHotkey)
    }

    Column(
        modifier = modifier
    ) {

        EditText(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(36.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    L.d("onSearch $searchText")
                    doSearch(searchText, pagingItems)
                }
            ),
            placeholder = "搜索",
//            leadingIcon = IconFont.Search,
            trailingIcon = IconFont.Search,
            onTrailingClick = {
                doSearch(searchText, pagingItems)
            }
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (searchState.inSearching) {
                LazyColumn(
                    state = searchResultLazyListState
                ) {
                    items(pagingItems) { article ->
                        ArticleItem(
                            article = article!!,
                            showCollect = false,
                            clickAction = {
                                WebViewActivity.start(context, article.link)
                            },
                            collectCallback = { art, collect ->
//                                vm.dispatch(MainEvent.Collect(art, collect, CollectSource.Wechat))
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground)
                        )
                    }

                    createRefreshItem(pagingItems)

                    createLoadingItem(pagingItems)
                }
            } else {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 0.dp, alignment = Alignment.Top),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)
                ) {
                    searchState.hotkeys.forEach { child ->
                        AssistChip(
                            onClick = {
                                searchText = child.name
                                doSearch(child.name, pagingItems)
                            },
                            label = {
                                Text(
                                    text = child.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = MaterialTheme.colorScheme.onBackground
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                borderColor = MaterialTheme.colorScheme.onBackground,
                                borderWidth = 1.dp
                            ),
                            elevation = null,
                            shape = MaterialTheme.shapes.large
                        )
                    }
                }
            }
        }
    }
}