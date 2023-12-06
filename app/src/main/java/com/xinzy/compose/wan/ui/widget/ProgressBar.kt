package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.util.L

@Composable
fun ProgressBar(
    progress: Int,
    modifier: Modifier = Modifier,
    max: Int = 100,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiary,
    progressColor: Color = MaterialTheme.colorScheme.onTertiary
) {
    L.d("ProgressBar progress=$progress")
    Canvas(modifier = modifier) {
        val height = size.height
        val width = size.width

        val path = Path()
        path.addRoundRect(RoundRect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height,
            radiusX = height / 2,
            radiusY = height / 2
        ))
        drawPath(
            path = path,
            color = backgroundColor
        )

        val length = progress * width / max
        path.reset()
        path.addRoundRect(RoundRect(
            left = 0f,
            top = 0f,
            right = length,
            bottom = height,
            radiusX = height / 2,
            radiusY = height / 2
        ))
        drawPath(
            path = path,
            color = progressColor
        )
    }
}

@Preview
@Composable
fun ProgressBarPreview() {
    Box(
        modifier = Modifier.size(width = 250.dp, height = 100.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {

        ProgressBar(
            progress = 50,
            modifier = Modifier
                .width(200.dp)
                .height(3.dp),
            backgroundColor = Color.Green,
            progressColor = Color.Red
        )
    }
}