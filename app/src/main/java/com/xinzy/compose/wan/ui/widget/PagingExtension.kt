package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.lazy.LazyListScope
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.xinzy.compose.wan.util.L

const val LoadingContentType = "ContentLoading"

val <T: Any> LazyPagingItems<T>.isRefreshing: Boolean get() = this.loadState.refresh == LoadState.Loading
val <T: Any> LazyPagingItems<T>.isLoading: Boolean get() = this.loadState.append == LoadState.Loading

fun <T: Any> LazyListScope.createRefreshItem(items: LazyPagingItems<T>) {
    when (val state = items.loadState.refresh) {
        is LoadState.Error -> {
            L.d("refresh fail ${state.error.message}")
        }
        LoadState.Loading -> {
            item(
                contentType = LoadingContentType
            ) {
                LoadingItem()
            }
        }
        is LoadState.NotLoading -> {
            L.d("not refreshing")
        }
    }
}

fun <T: Any> LazyListScope.createLoadingItem(items: LazyPagingItems<T>) {
    when (val state = items.loadState.append) {
        is LoadState.Error -> {
            L.d("refresh fail ${state.error.message}")
        }
        LoadState.Loading -> {
            item(
                contentType = LoadingContentType
            ) {
                LoadingItem()
            }
        }
        is LoadState.NotLoading -> {
            L.d("not refreshing")
        }
    }
}