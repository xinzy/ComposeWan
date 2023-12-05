package com.xinzy.compose.wan.http

sealed class HttpResult<out T : Any> {

    data class Success<out T : Any>(val data: T): HttpResult<T>()

    data class Failure(val code: Int, val msg: String) : HttpResult<Nothing>()
}
