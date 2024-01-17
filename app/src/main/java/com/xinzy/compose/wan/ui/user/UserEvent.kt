package com.xinzy.compose.wan.ui.user

import com.xinzy.compose.wan.entity.Favor

sealed class UserEvent {

    data class Login(
        val username: String,
        val password: String,
    ) : UserEvent()

    data class Register(
        val username: String,
        val password: String,
        val confirm: String,
    ) : UserEvent()

    object Logout : UserEvent()

    object Score: UserEvent()

    class UnCollect(
        val favor: Favor
    ) : UserEvent()
}