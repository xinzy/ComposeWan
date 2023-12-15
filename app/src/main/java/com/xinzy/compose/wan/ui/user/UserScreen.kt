package com.xinzy.compose.wan.ui.user

import androidx.activity.compose.BackHandler
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

    // 点击返回的处理
    fun onBackHandler() {
        when (typeState) {
            UserUiType.Register -> typeState = UserUiType.Login

            UserUiType.Login,
            UserUiType.Mine -> activity.finish()

            UserUiType.Favor,
            UserUiType.Message,
            UserUiType.Rank,
            UserUiType.Score -> typeState = UserUiType.Mine
        }
    }

    BackHandler(onBack = ::onBackHandler)

    Column {
        val title = typeState.title
        WanTopAppBar(
            title = title,
            navigationIcon = IconFont.Back,
            onNavigationAction = {
                onBackHandler()
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
                    onScoreClick = {
                        typeState = UserUiType.Score
                    },
                    onRankClick = {
                        typeState = UserUiType.Rank
                    },
                    onFavorClick = {
                        typeState = UserUiType.Favor
                    },
                    onMessageClick = {
                        typeState = UserUiType.Message
                    }
                )
            }

            UserUiType.Favor -> {

            }

            UserUiType.Message -> {

            }

            UserUiType.Rank -> {

            }

            UserUiType.Score -> {
                ScoreScreen(
                    vm = vm,
                    activity = activity
                )
            }
        }
    }
}