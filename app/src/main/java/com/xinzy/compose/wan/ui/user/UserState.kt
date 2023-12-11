package com.xinzy.compose.wan.ui.user

data class UserState<T>(
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null
)