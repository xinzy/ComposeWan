package com.xinzy.compose.wan.entity

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
    @SerializedName("errorCode")
    val code: Int,
    @SerializedName("errorMsg")
    val message: String,
    val data: T?
) {

    val isSuccess: Boolean get() = code == 0
}