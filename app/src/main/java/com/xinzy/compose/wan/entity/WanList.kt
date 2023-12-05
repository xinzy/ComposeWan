package com.xinzy.compose.wan.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WanList<T>(
    @SerializedName("curPage")
    var page: Int = 0,
    var offset: Int = 0,
    var over: Boolean = false,
    var pageCount: Int = 0,
    var size: Int = 0,
    var total: Int = 0,
    private var datas: List<T>? = null
) {
    fun getData() = datas ?: listOf()
}