package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


data class BannerIndicator(
    val selectedColor: Color,
    val defaultColor: Color
)

@Composable
fun defaultBannerIndicator() =
    BannerIndicator(
        selectedColor = MaterialTheme.colorScheme.onErrorContainer,
        defaultColor = MaterialTheme.colorScheme.errorContainer
    )

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> Banner(
    items: List<T>,
    modifier: Modifier = Modifier,
    infiniteScroll: Boolean = true,
    timeMillis: Long = 3000,
    indicator: BannerIndicator = defaultBannerIndicator(),
    itemContent: @Composable (item: T) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        val size = if (infiniteScroll) Int.MAX_VALUE else items.size
        val pageState = rememberPagerState { size }

        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(867f / 482)
        ) {
            itemContent(items[it % items.size])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items.forEachIndexed { index, _ ->
                val itemColor = if (index == pageState.currentPage % items.size) indicator.selectedColor else indicator.defaultColor

                Spacer(modifier = Modifier.width(2.dp))

                Box(
                    modifier = Modifier
                        .size(width = 6.dp, height = 6.dp)
                        .clip(CircleShape)
                        .background(color = itemColor)
                )

                Spacer(modifier = Modifier.width(2.dp))
            }
        }

        if (pageState.pageCount > 0) {
            LaunchedEffect(key1 = pageState.currentPage) {
                delay(timeMillis)
                if (infiniteScroll || pageState.currentPage < pageState.pageCount - 1) {
                    pageState.animateScrollToPage(pageState.currentPage + 1)
                } else {
                    pageState.animateScrollToPage(0)
                }
            }
        }
    }
}


@Composable
@Preview
fun BannerPreview() {
    val banners = listOf(1, 2, 3)

    Banner(
        items = banners,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.White),
        indicator = BannerIndicator(
            selectedColor = Color.Red,
            defaultColor = Color.Gray
        )
    ) {
        Box(modifier = Modifier.background(color = Color.White))
        Text(
            text = it.toString(),
            color = Color.Black
        )
    }
}
