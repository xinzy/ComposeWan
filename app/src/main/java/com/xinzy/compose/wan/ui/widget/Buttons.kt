package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.xinzy.compose.wan.ui.theme.WanColors
import com.xinzy.compose.wan.util.IconFont

@Composable
fun CenteredButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val m = if (enabled) modifier.clickable { onClick() } else modifier

    Box(
        modifier = m,
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun IconFontButton(
    icon: IconFont,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = { },
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    CenteredButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        IconFontText(
            icon = icon,
            style = style,
            color = color
        )
    }
}

@Composable
@Preview
fun IconFontButtonPreview() {
    IconFontButton(icon = IconFont.Author)
}

@Composable
fun IconTextButton(
    icon: IconFont,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    iconStyle: TextStyle = MaterialTheme.typography.titleLarge,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = iconColor
) {
    CenteredButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ConstraintLayout {
            val (iconText, titleText) = createRefs()

            IconFontText(
                modifier = Modifier.constrainAs(iconText) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                },
                icon = icon,
                style = iconStyle,
                color = iconColor
            )

            Text(
                modifier = Modifier.constrainAs(titleText) {
                    centerHorizontallyTo(iconText)
                    top.linkTo(iconText.bottom, 8.dp)
                },
                text = text,
                style = titleStyle,
                color = titleColor
            )
        }
    }
}

@Composable
@Preview
fun IconTextButtonPreview() {
    IconTextButton(
        icon = IconFont.Author,
        text = "测试",
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = WanColors.transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondary
        ),
        contentPadding = contentPadding,
        content = content
    )
}