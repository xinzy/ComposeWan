package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.ui.widget.WanTopAppBar
import com.xinzy.compose.wan.util.IconFont

@Composable
fun UserScreen(
    type: UserUiType,
    modifier: Modifier = Modifier,
    activity: UserActivity,
    vm: UserViewModel = viewModel()
) {
    var typeState by remember {
        mutableStateOf(type)
    }

    Column {
        val title = typeState.title
        WanTopAppBar(
            title = title,
            navigationIcon = IconFont.Back,
            onNavigationAction = {
                activity.finish()
            }
        )

        when (typeState) {
            UserUiType.Login -> {
                LoginScreen(
                    vm = vm,
                    activity = activity,
                    onChangeType = {
                        typeState = it
                    }
                )
            }
            UserUiType.Register -> {
                RegisterScreen(
                    vm = vm,
                    activity = activity,
                    onChangeType = {
                        typeState = it
                    }
                )
            }
            UserUiType.Mine -> {
                MineScreen(
                    vm = vm,
                    activity = activity,
                )
            }

            UserUiType.Favor -> {

            }

            UserUiType.Message -> {

            }

            UserUiType.Rank -> {

            }

            UserUiType.Score -> {

            }
        }
    }
}