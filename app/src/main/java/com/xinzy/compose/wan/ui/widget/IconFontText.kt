package com.xinzy.compose.wan.ui.widget

import android.graphics.Typeface
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.util.IconFont

@Composable
fun IconFontText(
    icon: IconFont,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        color = color,
        text = icon.text,
        fontSize = fontSize,
        fontFamily = FontFamily(Typeface.createFromAsset(LocalContext.current.assets, "fonts/iconfont.ttf")),
        style = style,
        textAlign = TextAlign.Center
    )
}



@Composable
@Preview
fun IconFontTextPreview() {
    IconFontText(
        icon = IconFont.Author,
        color = Color.Red
    )
}