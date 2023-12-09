package com.xinzy.compose.wan.ui.user

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.widget.WanTopAppBar

@Composable
fun UserScreen(
    type: Int,
    modifier: Modifier = Modifier,
    activity: UserActivity,
    vm: UserViewModel = viewModel()
) {
    var typeState by remember {
        mutableIntStateOf(type)
    }

    Column {
        val title = if (typeState == UserActivity.TYPE_REGISTER) "注册" else "登录"
        WanTopAppBar(
            title = title,
            navigationIcon = R.string.icon_back,
            onNavigationAction = {
                activity.finish()
            }
        )

        if (typeState == UserActivity.TYPE_REGISTER) {
            RegisterScreen(
                vm = vm,
                activity = activity,
                onChangeType = {
                    typeState = it
                }
            )
        } else {
            LoginScreen(
                vm = vm,
                activity = activity,
                onChangeType = {
                    typeState = it
                }
            )
        }
    }
}