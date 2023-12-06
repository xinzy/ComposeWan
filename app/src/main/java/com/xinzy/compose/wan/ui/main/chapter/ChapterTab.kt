package com.xinzy.compose.wan.ui.main.chapter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.widget.ProgressDialog
import com.xinzy.compose.wan.ui.widget.TitleBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChapterTab(
    tab: MainTabs,
    modifier: Modifier = Modifier,
    vm: ChapterViewModel = viewModel()
) {
    val viewState = vm.viewState
    val isRefresh = viewState.refreshing
    val chapters = viewState.chapters

    Column(
        modifier = modifier
    ) {
        TitleBar(title = tab.title)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (chapters.isNotEmpty()) {
                LazyColumn {
                    items(
                        items = chapters
                    ) { chapter ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.onSecondary)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            text = chapter.name,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 0.dp, alignment = Alignment.Top),
                            horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)
                        ) {
                            chapter.children?.forEach { child ->
                                AssistChip(
                                    onClick = {
                                    },
                                    label = {
                                        Text(
                                            text = child.name,
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
                    }
                }
            }

            if (isRefresh) {
                ProgressDialog()
            }
        }
    }
}


@Preview
@Composable
fun ChapterTabPreview() {
    ChapterTab(tab = MainTabs.Chapter)
}

