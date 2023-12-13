package com.xinzy.compose.wan.ui.user

enum class UserUiType(val type: Int, val title: String) {

    Login(type = 1, title = "登录"),
    Register(type = 2, title = "注册"),
    Mine(type = 3, title = "我的"),
    Favor(type = 4, title = "收藏"),
    Message(type = 5, title = "消息"),
    Rank(type = 6, title = "排行"),
    Score(type = 7, title = "积分"),


    ;

    companion object {
        fun from(type: Int): UserUiType = values().firstOrNull { it.type == type } ?: Login
    }
}