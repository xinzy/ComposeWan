package com.xinzy.compose.wan.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xinzy.compose.wan.ui.theme.WanColors
import com.xinzy.compose.wan.util.IconFont


@Composable
fun WanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    label: @Composable (() -> Unit)? = null,
    placeholder: String = "",
    leadingIcon: IconFont? = null,
    trailingIcon: IconFont? = null,
    onTrailingClick: () -> Unit = { },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            leadingIcon?.let {
                IconFontButton(
                    modifier = Modifier
                        .height(56.dp)
                        .aspectRatio(1f),
                    icon = it,
                    enabled = false
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                IconFontButton(
                    modifier = Modifier
                        .height(56.dp)
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

data class EditTextColor(
    val focusedContainerColor: Color,
    val focusedTextColor: Color,
    val focusedShapeColor: Color,
    val unfocusedContainerColor: Color,
    val unfocusedTextColor: Color,
    val unfocusedShapeColor: Color,
    val cursorColor: Color,
    val disableContainerColor: Color,
    val disableTextColor: Color,
    val disableShapeColor: Color,
    val readonlyContainerColor: Color,
    val readonlyTextColor: Color,
    val readonlyShapeColor: Color,
) {

    @Composable
    fun textColor(
        enabled: Boolean,
        readOnly: Boolean,
        interactionSource: InteractionSource
    ): Color {
        val focus by interactionSource.collectIsFocusedAsState()
        return when {
            !enabled -> disableTextColor
            readOnly -> readonlyTextColor
            focus -> focusedTextColor
            else -> unfocusedTextColor
        }
    }

    @Composable
    fun containerColor(
        enabled: Boolean,
        readOnly: Boolean,
        interactionSource: InteractionSource
    ): Color {
        val focus by interactionSource.collectIsFocusedAsState()
        return when {
            !enabled -> disableContainerColor
            readOnly -> readonlyContainerColor
            focus -> focusedContainerColor
            else -> unfocusedContainerColor
        }
    }

    @Composable
    fun shapeColor(
        enabled: Boolean,
        readOnly: Boolean,
        interactionSource: InteractionSource
    ): Color {
        val focus by interactionSource.collectIsFocusedAsState()
        return when {
            !enabled -> disableShapeColor
            readOnly -> readonlyShapeColor
            focus -> focusedShapeColor
            else -> unfocusedShapeColor
        }
    }

    companion object {

        @Composable
        fun colors(
            focusedContainerColor: Color = MaterialTheme.colorScheme.background,
            focusedTextColor: Color = MaterialTheme.colorScheme.onBackground,
            focusedShapeColor: Color = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor: Color = focusedContainerColor,
            unfocusedTextColor: Color = focusedTextColor,
            unfocusedShapeColor: Color = focusedShapeColor,
            cursorColor: Color = focusedTextColor,
            disableContainerColor: Color = focusedContainerColor,
            disableTextColor: Color = focusedTextColor,
            disableShapeColor: Color = focusedShapeColor,
            readonlyContainerColor: Color = focusedContainerColor,
            readonlyTextColor: Color = focusedTextColor,
            readonlyShapeColor: Color = focusedShapeColor,
        ): EditTextColor = EditTextColor(
            focusedContainerColor = focusedContainerColor,
            focusedTextColor = focusedTextColor,
            focusedShapeColor = focusedShapeColor,
            unfocusedContainerColor = unfocusedContainerColor,
            unfocusedTextColor = unfocusedTextColor,
            unfocusedShapeColor = unfocusedShapeColor,
            cursorColor = cursorColor,
            disableContainerColor = disableContainerColor,
            disableTextColor = disableTextColor,
            disableShapeColor = disableShapeColor,
            readonlyContainerColor = readonlyContainerColor,
            readonlyTextColor = readonlyTextColor,
            readonlyShapeColor = readonlyShapeColor
        )
    }
}

@Composable
fun EditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = { },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: EditTextColor = EditTextColor.colors(),
    shape: Shape = RoundedCornerShape(16.dp),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    placeholder: String? = "",
    leadingIcon: IconFont? = null,
    onLeadingClick: () -> Unit = { },
    trailingIcon: IconFont? = null,
    onTrailingClick: () -> Unit = { },
) {
    val textColor = colors.textColor(enabled = enabled, readOnly = readOnly, interactionSource = interactionSource)
    val containerColor = colors.containerColor(enabled = enabled, readOnly = readOnly, interactionSource = interactionSource)
    val shapeColor = colors.shapeColor(enabled = enabled, readOnly = readOnly, interactionSource = interactionSource)

    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(colors.cursorColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .background(color = containerColor)
                    .border(width = 1.dp, color = shapeColor, shape = shape)
                    .padding(paddingValues = paddingValues),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let { 
                    IconFontButton(
                        icon = it,
                        enabled = false,
                        onClick = onLeadingClick,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        style = mergedTextStyle
                    )
                }

                val paddingStart = if (leadingIcon == null) 16.dp else 0.dp
                val paddingEnd = if (trailingIcon == null) 16.dp else 0.dp

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(
                            start = paddingStart,
                            end = paddingEnd
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        innerTextField()
                    }

                    if (value.isEmpty()) {
                        placeholder?.let {
                            Text(
                                text = it,
                                modifier = Modifier.fillMaxWidth(),
                                style = mergedTextStyle,
                                color = textColor.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                trailingIcon?.let {
                    IconFontButton(
                        icon = it,
                        onClick = onTrailingClick,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        style = mergedTextStyle
                    )
                }
            }
        }
    )
}

@Composable
@Preview
private fun EditTextPreview() {

    EditText(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .width(200.dp)
            .height(36.dp),
        singleLine = true,
        leadingIcon = IconFont.Search,
        trailingIcon = IconFont.Close,
    )
}