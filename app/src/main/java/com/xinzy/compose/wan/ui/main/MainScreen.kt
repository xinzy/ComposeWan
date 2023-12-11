package com.xinzy.compose.wan.ui.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.main.chapter.ChapterTab
import com.xinzy.compose.wan.ui.main.home.HomeTab
import com.xinzy.compose.wan.ui.main.nav.NavTab
import com.xinzy.compose.wan.ui.main.project.ProjectTab
import com.xinzy.compose.wan.ui.main.wechat.WechatTab
import com.xinzy.compose.wan.ui.theme.WanColors
import com.xinzy.compose.wan.ui.theme.WanShapes
import com.xinzy.compose.wan.ui.user.UserActivity
import com.xinzy.compose.wan.ui.user.UserUiType
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.WanTopAppBar
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

    val items = MainTabs.tabs
    var currentSelected by remember { mutableStateOf(items[0]) }

    var isOpenDrawer by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange = {
            val isOpen = it == DrawerValue.Open
            if (isOpenDrawer != isOpen) {
                isOpenDrawer = isOpen
            }
            true
        }
    )

    LaunchedEffect(key1 = isOpenDrawer) {
        if (isOpenDrawer) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.6f)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                MainDrawer(
                    onDismissDrawer = {
                        if (!isOpenDrawer) {
                            isOpenDrawer = false
                        }
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize(),
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                WanTopAppBar(
                    title = currentSelected.title,
                    navigationIcon = IconFont.Menu,
                    onNavigationAction = {
                        isOpenDrawer = true
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    items.forEachIndexed { _, tab ->
                        NavigationBarItem(
                            selected = currentSelected == tab,
                            onClick = {
                                currentSelected = tab
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = tab.icon),
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    }
                }
            }
        ) { values ->
            when (currentSelected) {
                MainTabs.Main -> {
                    HomeTab(
                        tab = MainTabs.Main,
                        modifier = Modifier.padding(values)
                    )
                }

                MainTabs.Project -> {
                    ProjectTab(
                        tab = MainTabs.Project,
                        modifier = Modifier.padding(values)
                    )
                }
                MainTabs.Chapter -> {
                    ChapterTab(
                        tab = MainTabs.Chapter,
                        modifier = Modifier.padding(values)
                    )
                }
                MainTabs.Nav -> {
                    NavTab(
                        tab = MainTabs.Nav,
                        modifier = Modifier.padding(values)
                    )
                }
                MainTabs.WeChat -> {
                    WechatTab(
                        tab = MainTabs.WeChat,
                        modifier = Modifier.padding(values)
                    )
                }
            }
        }
    }
}

@Composable
fun MainDrawer(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onDismissDrawer: () -> Unit = { },
) {
    val loginState = User.me().loginState

    val drawerItems = DrawerItems.values()

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, bottom = 24.dp)
        ) {
            if (loginState) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            UserActivity.start(context, UserUiType.Mine)
                            onDismissDrawer()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    IconFontText(
                        icon = IconFont.Avatar,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            UserActivity.start(context, UserUiType.Mine)
                            onDismissDrawer()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = User.me().nickname,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconFontText(
                        modifier = Modifier
                            .clickable {
                                UserActivity.start(context, UserUiType.Login)
                                onDismissDrawer()
                            },
                        icon = IconFont.Avatar,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                UserActivity.start(context, UserUiType.Login)
                                onDismissDrawer()
                            },
                        text = "未登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        ModalDrawerSheet(
            drawerShape = WanShapes.none,
            drawerContainerColor = WanColors.transparent,
            drawerContentColor = WanColors.transparent
        ) {

            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = false,
                    onClick = {

                    },
                    icon = {
                        IconFontText(
                            icon = item.icon,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = WanShapes.none
                )
            }
        }
    }
}

@Composable
@Preview
fun MainDrawerPreview() {
    MainDrawer()
}

//@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}