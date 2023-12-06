package com.xinzy.compose.wan.ui.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private fun webViewClient(
    onPageStart: ((view: WebView, url: String) -> Unit)? = null,
    onPageFinish: ((view: WebView, url: String) -> Unit)? = null,
    shouldOverrideUrlLoading: ((view: WebView, request: WebResourceRequest) -> Boolean)? = null
): WebViewClient = object : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return shouldOverrideUrlLoading?.invoke(view, request) ?: super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        onPageStart?.invoke(view, url) ?: super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView, url: String) {
        onPageFinish?.invoke(view, url) ?: super.onPageFinished(view, url)
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//        super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }
}

private fun webChromeClient(
    onReceivedIcon: ((view: WebView, icon: Bitmap) -> Unit)? = null,
    onReceivedTitle: ((view: WebView, title: String) -> Unit)? = null,
    onProgressChanged: ((view: WebView, newProgress: Int) -> Unit)? = null
): WebChromeClient = object : WebChromeClient() {

    override fun onReceivedIcon(view: WebView, icon: Bitmap) {
        onReceivedIcon?.invoke(view, icon) ?: super.onReceivedIcon(view, icon)
    }

    override fun onReceivedTitle(view: WebView, title: String) {
        onReceivedTitle?.invoke(view, title) ?: super.onReceivedTitle(view, title)
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        onProgressChanged?.invoke(view, newProgress) ?: super.onProgressChanged(view, newProgress)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ComposeWebView(
    url: String,
    modifier: Modifier = Modifier,
    update: ((WebView) -> Unit)? = null,
    isDarkTheme: Boolean = true,
    onPageStart: ((view: WebView, url: String) -> Unit)? = null,
    onPageFinish: ((view: WebView, url: String) -> Unit)? = null,
    shouldOverrideUrlLoading: ((view: WebView, request: WebResourceRequest) -> Boolean)? = null,
    onReceivedIcon: ((view: WebView, icon: Bitmap) -> Unit)? = null,
    onReceivedTitle: ((view: WebView, title: String) -> Unit)? = null,
    onProgressChanged: ((view: WebView, newProgress: Int) -> Unit)? = null
) {
    val webViewBackground = MaterialTheme.colorScheme.background.value.toInt()
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = webViewClient(
                    onPageStart = onPageStart,
                    onPageFinish = onPageFinish,
                    shouldOverrideUrlLoading = shouldOverrideUrlLoading
                )

                webChromeClient = webChromeClient(
                    onReceivedIcon = onReceivedIcon,
                    onProgressChanged = onProgressChanged,
                    onReceivedTitle = onReceivedTitle
                )

                settings.javaScriptEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.setSupportZoom(true)

                setBackgroundColor(webViewBackground)

                loadUrl(url)
            }
        },
        modifier = modifier,
        update = {
            update?.invoke(it)
        }
    )
}