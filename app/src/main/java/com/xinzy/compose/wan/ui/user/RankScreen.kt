package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinzy.compose.wan.entity.Rank
import com.xinzy.compose.wan.ui.theme.WanColors
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.L

@Composable
fun RankScreen(
    vm: UserViewModel,
    modifier: Modifier = Modifier
) {
    val rankPagingItems = vm.rankPager.collectAsLazyPagingItems()


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            isRefreshing = rankPagingItems.isRefreshing,
            onRefresh = {
                L.d("onRefresh")
                rankPagingItems.refresh()
            }
        ) {
            LazyColumn {
                items(rankPagingItems) { item ->
                    RankItem(rank = item!!)
                }

                createRefreshItem(rankPagingItems)

                createLoadingItem(rankPagingItems)
            }
        }
    }
}

@Composable
private fun RankItem(
    rank: Rank
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rankText = rank.rank.toString()

        Text(
            modifier = Modifier
                .width(32.dp),
            text = rankText,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = when (rankText.length) {
                1 -> MaterialTheme.typography.bodyLarge
                2 -> MaterialTheme.typography.bodyMedium
                else -> MaterialTheme.typography.bodySmall
            },
            color = when (rank.rank) {
                1 -> WanColors.gold
                2 -> WanColors.silver
                3 -> WanColors.copper
                else -> MaterialTheme.colorScheme.onBackground
            }
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = rank.username,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "等级: ${rank.level}",
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Text(
            modifier = Modifier.width(64.dp),
            text = rank.coinCount.toString(),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style =  MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
@Preview
private fun RankPreview() {
    RankItem(
        rank = Rank(
            coinCount = 12345,
            level = 2341,
            nickname = "",
            rank = 12345,
            userId = 111,
            username = "RankText"
        )
    )
}