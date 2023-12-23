package com.xinzy.compose.wan.http

import com.xinzy.compose.wan.entity.ApiResult

class WanHttpException(
    val code: Int,
    message: String
) : Exception(message)

fun <T> ApiResult<T>.toException(): WanHttpException = WanHttpException(this.code, this.message)