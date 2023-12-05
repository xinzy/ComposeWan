package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.lazy.LazyListState

val LazyListState.needLoadingMore: Boolean
    get() = !isScrollInProgress && !canScrollForward