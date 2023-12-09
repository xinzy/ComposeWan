package com.xinzy.compose.wan.ui.main.mine

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.main.MainTabs
import com.xinzy.compose.wan.ui.widget.TitleBar
import java.lang.Float.max
import kotlin.Float

@Composable
fun MineTab(
    tab: MainTabs,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading(
                modifier = Modifier.size(50.dp),
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun MineTabPreview() {
    MineTab(tab = MainTabs.WeChat)
}

@Composable
private fun Loading(
    modifier: Modifier,
    color: Color = Color.Black
) {
    val showAnimator by remember { mutableStateOf(true) }
    val animatorDegrees = remember {
        Animatable(0f)
    }
    val animationSpec = infiniteRepeatable(
        animation = tween<Float>(
            durationMillis = 1000,
            easing = LinearEasing
        ),
    )

    LaunchedEffect(showAnimator) {
        animatorDegrees.animateTo(
            targetValue = 360f,
            animationSpec = animationSpec
        )
    }

    Canvas(
        modifier = modifier
    ) {
        val width = size.width
        val height = size.height

        val r = max(1f, width / 5)
        val itemCount = 12

        val path = Path()
        path.addRoundRect(
            RoundRect(
                rect = Rect(Offset(width / 2 - r / 10, 0f), Size(r / 5, r)),
                cornerRadius = CornerRadius(r / 5, r / 5)
            )
        )

        (0 until itemCount).forEach {
            val degrees = 30f * it + animatorDegrees.value
            rotate(degrees) {
                drawPath(path = path, color = color.copy(alpha = (it + 1f) / itemCount))
            }
        }
    }
}