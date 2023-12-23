package com.xinzy.compose.wan.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WanList<T>(
    @SerializedName("curPage")
    val page: Int = 0,
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
    val datas: List<T>
) {
    val isFirstPage: Boolean get() = offset == 0
    val isLastPage: Boolean get() = over

    val prevPage: Int? get() = if (isFirstPage) null else page - 1
    val nextPage: Int? get() = if (isLastPage) null else page + 1
}