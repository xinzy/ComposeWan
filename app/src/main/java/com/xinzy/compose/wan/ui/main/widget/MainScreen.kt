package com.xinzy.compose.wan.ui.main.widget

import androidx.compose.foundation.layout.fillMaxSize
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
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.main.chapter.ChapterTab
import com.xinzy.compose.wan.ui.main.home.HomeTab
import com.xinzy.compose.wan.ui.main.mine.MineTab
import com.xinzy.compose.wan.ui.main.project.ProjectTab

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
        println(values)
        when (currentSelected.value) {
            MainTabs.Main -> {
                HomeTab(tab = MainTabs.Main)
            }

            MainTabs.Project -> {
                ProjectTab(tab = MainTabs.Project)
            }
            MainTabs.Chapter -> {
                ChapterTab(tab = MainTabs.Chapter)
            }
            MainTabs.Mine -> {
                MineTab(tab = MainTabs.Mine)
            }
        }
    }
}



@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}