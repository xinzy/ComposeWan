package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.theme.Typography
import com.xinzy.compose.wan.util.IconFont

fun interface OnArticleCollectCallback {
    fun onArticleCollect(article: Article, collect: Boolean)
}

@Composable
fun ArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
    showCollect: Boolean = User.me().isLogin,
    callback: OnArticleCollectCallback? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showCollect) {

            IconFontButton(
                icon = if (article.collect) IconFont.Favor else IconFont.UnFavor,
                onClick = {
                    callback?.onArticleCollect(article, !article.collect)
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (article.collect) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = modifier.width(12.dp))
        }

        Column(
            modifier = modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconFontText(
                    icon = if (article.hasAuthor) IconFont.Author else IconFont.SharedUser,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = article.displayAuthor,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = article.getCategory(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = modifier.height(6.dp))

            Text(
                text = article.displayTitle,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                style = Typography.titleMedium
            )

            Spacer(modifier = modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (article.isTop()) {
                        IconFontText(
                            IconFont.Top,
                            color = MaterialTheme.colorScheme.errorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.size(4.dp))
                    }

                    if (article.fresh) {
                        IconFontText(
                            IconFont.New,
                            color = MaterialTheme.colorScheme.errorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.size(4.dp))
                    }

                    article.tags.forEach {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier
                                .height(16.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = it.name,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 9.sp
                            )
                        }

                        Spacer(modifier = Modifier.size(4.dp))
                    }
                }

                Text(
                    text = article.niceDate,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

}


@Composable
@Preview
fun ArticleItemPreview() {
    ArticleItem(
        article = Article(
            author = "巴拉巴拉",
            title = "测试吧啦巴拉巴拉",
            desc = "我们现在有标题栏了，下面我们写页面主要内容，下面我们在MainActivity.kt中新增一个BodyContent()函数",
            superChapterName = "分类",
            chapterName = "公众号"
        )
    )
}