package com.xinzy.compose.wan.ui.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WanTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    @StringRes navigationIcon: Int? = null,
    onNavigationAction: (() -> Unit) = { }
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            navigationIcon?.let { icon ->
                IconFontButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    icon = icon,
                    onClick = onNavigationAction
                )
            }
        }
    )
}