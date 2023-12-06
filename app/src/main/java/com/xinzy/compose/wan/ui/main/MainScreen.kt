package com.xinzy.compose.wan.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.xinzy.compose.wan.ui.main.chapter.ChapterTab
import com.xinzy.compose.wan.ui.main.home.HomeTab
import com.xinzy.compose.wan.ui.main.mine.MineTab
import com.xinzy.compose.wan.ui.main.project.ProjectTab
import com.xinzy.compose.wan.util.L

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

    val items = MainTabs.values()
    val currentSelected = remember { mutableStateOf(items[0]) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { _, tab ->
                    NavigationBarItem(
                        selected = currentSelected.value == tab,
                        onClick = {
                            currentSelected.value = tab
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
        L.d("bottomPadding=${values.calculateBottomPadding()}, topPadding=${values.calculateTopPadding()}")
        when (currentSelected.value) {
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
            MainTabs.Mine -> {
                MineTab(
                    tab = MainTabs.Mine,
                    modifier = Modifier.padding(values)
                )
            }
        }
    }
}



@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}