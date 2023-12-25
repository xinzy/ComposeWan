package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.L

@Composable
fun ScoreScreen(
    vm: UserViewModel,
    modifier: Modifier = Modifier
) {
    val scoreRecordPager = vm.scoreRecordPager.collectAsLazyPagingItems()

    L.d("state=${scoreRecordPager.loadState.refresh}")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            isRefreshing = scoreRecordPager.isRefreshing,
            onRefresh = {
                L.d("onRefresh")
                scoreRecordPager.refresh()
            }
        ) {
            LazyColumn {
                items(scoreRecordPager) { record ->
                    record?.let {
                        RecordItem(
                            record = record,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                createRefreshItem(scoreRecordPager)

                createLoadingItem(scoreRecordPager)
            }
        }
    }
}

@Composable
private fun RecordItem(
    record: ScoreRecord,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (coinText, descText, reasonText, divider) = createRefs()

        Text(
            modifier = Modifier.constrainAs(coinText) {
                end.linkTo(parent.end, 16.dp)
                width = Dimension.preferredWrapContent
                centerVerticallyTo(parent)
            },
            text = record.coinCount.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onError
        )

        Text(
            modifier = Modifier.constrainAs(descText) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(coinText.start, 12.dp)
                top.linkTo(parent.top, 12.dp)
                width = Dimension.fillToConstraints
            },
            text = record.desc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            modifier = Modifier.constrainAs(reasonText) {
                start.linkTo(descText.start)
                top.linkTo(descText.bottom, 8.dp)
                bottom.linkTo(parent.bottom, 12.dp)
            },
            text = record.reason,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Spacer(
            modifier = Modifier
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(1.dp)
                }
                .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        )
    }
}

@Composable
@Preview
private fun RecordItemPreview() {
    RecordItem(
        record = ScoreRecord(
            desc = "2022-07-26 11:34:41 签到 , 积分：13 + 18",
            reason = "签到",
            coinCount = 21
        ),
        modifier = Modifier.width(420.dp)
    )
}