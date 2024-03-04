package com.xinzy.compose.wan.ui.main

import android.content.Context
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.search.SearchActivity
import com.xinzy.compose.wan.ui.user.UserUiType
import com.xinzy.compose.wan.util.IconFont

enum class MainTabs(val title: String, val icon: Int) {
    Main("主页", R.drawable.ic_navigation_main),
    WeChat("公众号", R.drawable.ic_navigation_weixin),
    Chapter("分类", R.drawable.ic_navigation_chapter),
    Project("项目", R.drawable.ic_navigation_project),
    Nav("导航", R.drawable.ic_navigation_nav),

    ;
}

enum class DrawerItems(val title: String, val icon: IconFont, val userUiType: UserUiType?, val action: ((Context) -> Unit)? = null) {
    Mine("我的", IconFont.Username, UserUiType.Mine),
    Score("我的积分", IconFont.Score, UserUiType.Score),
    Rank("积分排行", IconFont.Rank, UserUiType.Rank),
    Favor("我的收藏", IconFont.MyFavor, UserUiType.Favor),
    Message("我的消息", IconFont.Message, UserUiType.Message),

    /////////////////////////////////////////////////////////
    Search("搜索", IconFont.Search, null, { context -> SearchActivity.start(context) })

    ;
}