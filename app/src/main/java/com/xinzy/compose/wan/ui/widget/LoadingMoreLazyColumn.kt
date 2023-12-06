package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingMoreLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    autoLoading: Boolean = true,
    loadAction: (() -> Unit) = { },
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        content = content
    )

    if (autoLoading) {
        if (state.needLoadingMore) {
            loadAction()
        }
    }
}