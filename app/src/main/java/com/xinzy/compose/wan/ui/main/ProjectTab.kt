package com.xinzy.compose.wan.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.ui.widget.EditText
import com.xinzy.compose.wan.ui.widget.EditTextColor
import com.xinzy.compose.wan.ui.widget.SwipeRefresh
import com.xinzy.compose.wan.util.IconFont
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTab(
    tab: MainTabs,
    vm: MainViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(24.dp)
    ) {
        var text by remember { mutableStateOf("") }
        var readonly by remember { mutableStateOf(false) }
        var enabled by remember { mutableStateOf(true) }

        val colors = EditTextColor.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            disableContainerColor = MaterialTheme.colorScheme.tertiary,
            disableTextColor = MaterialTheme.colorScheme.onTertiary,
            readonlyContainerColor = MaterialTheme.colorScheme.error,
            readonlyTextColor = MaterialTheme.colorScheme.onError
        )

        EditText(
            value = text,
            onValueChange = {
                text = it
            },
            enabled = enabled,
            readOnly = readonly,
            modifier = Modifier
                .width(200.dp)
                .height(36.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { }),
            placeholder = "搜索",
            leadingIcon = IconFont.Search,
            trailingIcon = IconFont.Close,
            colors = colors
        )

        Button(onClick = {
            enabled = !enabled
        }) {
            Text(text = "Enable")
        }
        Button(onClick = {
            readonly = !readonly
        }) {
            Text(text = "ReadOnly")
        }
    }
}


@Preview
@Composable
fun ProjectTabPreview() {
    ProjectTab(tab = MainTabs.Project, vm = viewModel())
}