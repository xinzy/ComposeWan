package com.xinzy.compose.wan.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.xinzy.compose.wan.ui.theme.ComposeWanTheme

class WebViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url") ?: ""
        setContent {
            ComposeWanTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WebViewScreen(
                        url = url,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java).also {
                it.putExtra("url", url)
            }
            context.startActivity(intent)
        }
    }
}
