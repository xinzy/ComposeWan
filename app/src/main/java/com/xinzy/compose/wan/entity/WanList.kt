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
    private val datas: List<T>? = null
) {
    fun getData() = datas ?: listOf()
}