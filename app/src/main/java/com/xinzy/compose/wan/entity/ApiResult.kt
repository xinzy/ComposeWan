package com.xinzy.compose.wan.entity

data class ApiResult<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T?
) {

    val isSuccess: Boolean get() = errorCode == 0
}