package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.ui.theme.Typography

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: Article
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconFontText(
                resId = R.string.icon_author,
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

        Row {

            Column(
                modifier = Modifier.weight(1f)
            ) {

            }

            Text(
                text = article.niceDate,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
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