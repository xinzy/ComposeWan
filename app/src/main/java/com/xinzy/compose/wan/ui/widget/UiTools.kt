package com.xinzy.compose.wan.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


@Composable
fun ShowToast(
    msg: String,
    context: Context = LocalContext.current,
    length: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(context, msg, length).show()
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autoHideKeyboard(): Modifier = composed {
    val keyboard = LocalSoftwareKeyboardController.current
    pointerInput(this) {
        detectTapGestures(
            onPress = {
                keyboard?.hide()
            }
        )
    }
}