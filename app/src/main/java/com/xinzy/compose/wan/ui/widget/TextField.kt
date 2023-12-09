package com.xinzy.compose.wan.ui.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.theme.WanColors


@Composable
fun WanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    @StringRes leadingIcon: Int? = null,
    @StringRes trailingIcon: Int? = null,
    onTrailingClick: () -> Unit = { },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            leadingIcon?.let {
                IconFontButton(
                    modifier = Modifier.height(56.dp)
                        .aspectRatio(1f),
                    icon = it
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                IconFontButton(
                    modifier = Modifier.height(56.dp)
                        .aspectRatio(1f),
                    icon = it,
                    onClick = onTrailingClick
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = WanColors.transparent,
            unfocusedIndicatorColor = WanColors.transparent,
            disabledIndicatorColor = WanColors.transparent
        )
    )
}