package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.theme.Typography

@Composable
fun TitleBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit) = { }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = Typography.titleMedium,
                maxLines = 1
            )
        }

        if (showBackButton) {
            Button(
                onClick = onBackClick,
                contentPadding = PaddingValues()
            ) {
                IconFontText(
                    resId = R.string.icon_back,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }
        }

    }
}

@Composable
@Preview
fun TitleBarDisplay() {
    TitleBar(title = "标题", showBackButton = true)
}