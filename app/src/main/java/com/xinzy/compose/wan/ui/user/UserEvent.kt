package com.xinzy.compose.wan.ui.user

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

}