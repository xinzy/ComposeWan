package com.xinzy.compose.wan.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.widget.ProgressDialog

data class NavigationTabConfig(
    val currentSelectedIndex: MutableIntState,
    val leftLazyListState: LazyListState,
    val rightLazyListState: LazyListState
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigationTab(
    tab: MainTabs,
    vm: MainViewModel,
    modifier: Modifier = Modifier,
    config: NavigationTabConfig
) {
    val navigationState = vm.navigationViewState
    val navigationList = navigationState.data

    LaunchedEffect(key1 = Unit) {
        vm.dispatch(MainEvent.LoadNavigationChapter)
    }

    LaunchedEffect(key1 = config.currentSelectedIndex.intValue) {
        config.rightLazyListState.animateScrollToItem(config.currentSelectedIndex.intValue)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (navigationList.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .width(132.dp)
                        .fillMaxHeight(),
                    state = config.leftLazyListState
                ) {
                    itemsIndexed(navigationList) { index, navigation ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    config.currentSelectedIndex.intValue = index
                                }
                                .background(
                                    color = if (config.currentSelectedIndex.intValue == index) {
                                        MaterialTheme.colorScheme.onSecondary
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    }
                                )
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = navigation.name,
                                color = if (config.currentSelectedIndex.intValue == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = config.rightLazyListState
                ) {
                    items(navigationList) { navigation ->
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 0.dp, alignment = Alignment.Top),
                            horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)
                        ) {
                            navigation.articles.forEach { child ->
                                AssistChip(
                                    onClick = {
                                    },
                                    label = {
                                        Text(
                                            text = child.title,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        labelColor = MaterialTheme.colorScheme.onBackground
                                    ),
                                    border = AssistChipDefaults.assistChipBorder(
                                        borderColor = MaterialTheme.colorScheme.onBackground,
                                        borderWidth = 1.dp
                                    ),
                                    elevation = null,
                                    shape = MaterialTheme.shapes.large
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        if (navigationState.refreshing) {
            ProgressDialog()
        }
    }
}