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

@Composable
fun IconFontText(
    resId: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        color = color,
        text = LocalContext.current.getString(resId),
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
        resId = R.string.icon_back,
        color = Color.Red
    )
}