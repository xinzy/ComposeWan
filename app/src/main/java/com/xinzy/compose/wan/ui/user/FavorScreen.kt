package com.xinzy.compose.wan.ui.user

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinzy.compose.wan.entity.Favor
import com.xinzy.compose.wan.ui.web.WebViewActivity
import com.xinzy.compose.wan.ui.widget.IconFontButton
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.ui.widget.createLoadingItem
import com.xinzy.compose.wan.ui.widget.createRefreshItem
import com.xinzy.compose.wan.ui.widget.isRefreshing
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L
import com.xinzy.compose.wan.util.ToastUtil

private typealias FavorActionListener = ((Favor) -> Unit)

@Composable
fun FavorScreen(
    context: Context,
    vm: UserViewModel,
    modifier: Modifier = Modifier
) {
    val favorPagingItem = vm.favorPager.collectAsLazyPagingItems()

    val favorResult = vm.lastFavorResult()
    L.d("favor screen $favorResult")

    favorResult?.let {
        ToastUtil.show(it.message)
        if (it.success) {
            favorPagingItem.refresh()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            isRefreshing = favorPagingItem.isRefreshing,
            onRefresh = {
                L.d("onRefresh")
                favorPagingItem.refresh()
            }
        ) {
            LazyColumn {
                items(
                    items = favorPagingItem,
                    key = { it.id }
                ) { favor ->
                    FavorItem(
                        favor = favor!!,
                        itemClick = {
                            WebViewActivity.start(context, favor.link)
                        },
                        unCollect = {
                            vm.dispatch(UserEvent.UnCollect(it))
                        }
                    )
                }

                createRefreshItem(favorPagingItem)

                createLoadingItem(favorPagingItem)
            }
        }
    }
}

@Composable
private fun FavorItem(
    favor: Favor,
    itemClick: FavorActionListener? = null,
    unCollect: FavorActionListener? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itemClick?.invoke(favor)
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .width(18.dp)
                .height(18.dp),
            contentAlignment = Alignment.Center
        ) {
            IconFontButton(
                icon = IconFont.Favor,
                onClick = {
                    unCollect?.invoke(favor)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = favor.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconFontText(
                    icon = IconFont.Author,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = favor.author,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = favor.chapterName,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.weight(1f))


                Text(
                    text = favor.niceDate,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}