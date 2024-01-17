package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

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