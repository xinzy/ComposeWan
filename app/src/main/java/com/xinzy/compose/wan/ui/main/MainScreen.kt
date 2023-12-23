package com.xinzy.compose.wan.ui.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.theme.WanColors
import com.xinzy.compose.wan.ui.theme.WanShapes
import com.xinzy.compose.wan.ui.user.UserActivity
import com.xinzy.compose.wan.ui.user.UserUiType
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.WanTopAppBar
import com.xinzy.compose.wan.util.IconFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = viewModel()
) {
    val homeTabConfig = HomeTabConfig(lazyListState = rememberLazyListState())
    val chapterTabConfig = ChapterTabConfig(lazyListState = rememberLazyListState())
    val navigationTabConfig = NavigationTabConfig(
        currentSelectedIndex = remember { mutableIntStateOf(0) },
        leftLazyListState = rememberLazyListState(),
        rightLazyListState = rememberLazyListState()
    )
    val wechatTabConfig = WechatTabConfig(
        currentSelectedTabIndex = remember { mutableIntStateOf(0) }
    )

    val items = MainTabs.values()
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
                        if (isOpenDrawer) {
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
                if (currentSelected == MainTabs.Main || currentSelected == MainTabs.Chapter || currentSelected == MainTabs.Nav) {
                    WanTopAppBar(
                        title = currentSelected.title,
                        navigationIcon = IconFont.Menu,
                        onNavigationAction = {
                            isOpenDrawer = true
                        }
                    )
                }
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
                        vm = vm,
                        config = homeTabConfig,
                        modifier = Modifier.padding(values)
                    )
                }

                MainTabs.Project -> {
                    ProjectTab(
                        tab = MainTabs.Project,
                        vm = vm,
                        modifier = Modifier.padding(values)
                    )
                }
                MainTabs.Chapter -> {
                    ChapterTab(
                        tab = MainTabs.Chapter,
                        vm = vm,
                        config = chapterTabConfig,
                        modifier = Modifier.padding(values)
                    )
                }
                MainTabs.Nav -> {
                    NavigationTab(
                        tab = MainTabs.Nav,
                        vm = vm,
                        modifier = Modifier.padding(values),
                        config = navigationTabConfig
                    )
                }
                MainTabs.WeChat -> {
                    WechatTab(
                        tab = MainTabs.WeChat,
                        vm = vm,
                        modifier = Modifier.padding(values),
                        config = wechatTabConfig
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
    val coroutine = rememberCoroutineScope()

    fun startActivity(type: UserUiType) {
        UserActivity.start(context, type)
        coroutine.launch {
            delay(5000)
        }
        onDismissDrawer()
    }

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
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconFontText(
                        modifier = Modifier.clickable {
                            startActivity(UserUiType.Mine)
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
                        modifier = Modifier.clickable {
                            startActivity(UserUiType.Mine)
                        },
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
                                startActivity(UserUiType.Login)
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
                                startActivity(UserUiType.Login)
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
                        if (User.me().isLogin) {
                            startActivity(item.userUiType)
                        } else {
                            startActivity(UserUiType.Login)
                        }
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

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
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