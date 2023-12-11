package com.xinzy.compose.wan.ui.main

import androidx.annotation.StringRes
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.util.IconFont

enum class MainTabs(val title: String, val icon: Int) {
    Main("主页", R.drawable.ic_navigation_main),
    WeChat("微信", R.drawable.ic_navigation_weixin),
    Chapter("分类", R.drawable.ic_navigation_chapter),
    Project("项目", R.drawable.ic_navigation_project),
    Nav("导航", R.drawable.ic_navigation_nav),

    ;

    companion object {
        val tabs = listOf(
            Main, WeChat, Chapter, Project, Nav
        )
    }
}

enum class DrawerItems(val title: String, val icon: IconFont) {
    Mine("我的", IconFont.Message),

    ;
}