package com.xinzy.compose.wan.ui.user

data class UserState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = ""
)