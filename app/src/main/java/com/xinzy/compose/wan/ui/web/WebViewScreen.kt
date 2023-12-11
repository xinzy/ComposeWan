package com.xinzy.compose.wan.ui.web

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.theme.isDarkTheme
import com.xinzy.compose.wan.ui.widget.ComposeWebView
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.ProgressBar
import com.xinzy.compose.wan.ui.widget.TitleBar
import com.xinzy.compose.wan.ui.widget.WanTopAppBar
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.L


@Composable
fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        var title by remember {
            mutableStateOf("正在加载中...")
        }
        var showPageLoading by remember {
            mutableStateOf(true)
        }
        var loadingProgress by remember {
            mutableIntStateOf(0)
        }
        var webView: WebView

        WanTopAppBar(
            title = title
        )

        L.d("title=$title, showPageLoading=$showPageLoading, loadingProgress=$loadingProgress")

        Box(
            modifier = Modifier.weight(1f)
        ) {
            ComposeWebView(
                url = url,
                isDarkTheme = isDarkTheme(),
                update = { view -> webView = view },
                onPageStart = { _, _ -> showPageLoading = true},
                onPageFinish = { _, _ -> showPageLoading = false },
                onProgressChanged = { _, progress -> loadingProgress = progress },
                onReceivedTitle = { _, text -> title = text }
            )

            if (showPageLoading) {
                ProgressBar(
                    progress = loadingProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Button(onClick = {  }) {
                IconFontText(icon = IconFont.Back)
            }
        }
    }

}

@Composable
@Preview
fun WebViewScreenPreview() {

}