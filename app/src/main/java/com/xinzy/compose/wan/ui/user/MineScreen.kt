package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.widget.CenteredButton
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.PrimaryButton
import com.xinzy.compose.wan.ui.widget.SecondaryButton
import com.xinzy.compose.wan.util.IconFont
import com.xinzy.compose.wan.util.SimpleCallback
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(
    vm: UserViewModel,
    activity: UserActivity?,
    modifier: Modifier = Modifier
) {
    val score = vm.scoreState.data

    var showLogoutConfirm by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    var scoreLoaded by remember { mutableStateOf(false) }

    if (!scoreLoaded) {
        vm.dispatch(UserEvent.Score)
        scoreLoaded = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MineContent(
            modifier = Modifier.fillMaxSize(),
            score = score,
            onLogout = {
                showLogoutConfirm = true
            }
        )

        if (showLogoutConfirm) {
            ModalBottomSheet(
                onDismissRequest = {
                    showLogoutConfirm = false
                },
                sheetState = sheetState
            ) {

                LogoutTips(
                    sheetState = sheetState,
                    onCancelClick = {
                        showLogoutConfirm = false
                    },
                    onOkClick = {
                        showLogoutConfirm = false
                        vm.dispatch(UserEvent.Logout)

                        activity?.finish()
                    }
                )
            }
        }
    }

}

@Composable
private fun MineContent(
    modifier: Modifier = Modifier,
    score: Score? = null,
    onLogout: SimpleCallback
) {
    Column(
        modifier = modifier
            .padding(
                top = 64.dp
            )
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconFontText(
                icon = IconFont.Avatar,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = User.me().nickname,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 48.dp)
        ) {

            RankButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = IconFont.Rank,
                text = "排行",
                content = score?.rank?.toString() ?: "0",
                onClick = {

                }
            )

            RankButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = IconFont.Score,
                text = "积分",
                content = score?.coin?.toString() ?: "0",
                onClick = {

                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {

            DividerLine()

            SettingItem(
                icon = IconFont.MyFavor,
                text = "我的收藏"
            )

            DividerLine()

            SettingItem(
                icon = IconFont.Message,
                text = "我的消息"
            )

            DividerLine()

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            DividerLine()

            SettingItem(
                icon = IconFont.Logout,
                text = "退出登录",
                onClick = onLogout
            )

            DividerLine()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogoutTips(
    sheetState: SheetState,
    onCancelClick: SimpleCallback,
    onOkClick: SimpleCallback
) {
    val coroutine = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (tipText, cancelBtn, okBtn) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(tipText) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            text = "确定要退出登录么?",
            color = MaterialTheme.colorScheme.onError,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        createHorizontalChain(cancelBtn, okBtn, chainStyle = ChainStyle.Spread)

        SecondaryButton(
            modifier = Modifier
                .constrainAs(cancelBtn) {
                    top.linkTo(tipText.bottom, 24.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(okBtn.start, 16.dp)
                    bottom.linkTo(parent.bottom, 48.dp)
                    height = Dimension.value(32.dp)
                },
            onClick = {
                coroutine.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onCancelClick()
                }
            },
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
        ) {
            Text(
                text = "取消",
                style = MaterialTheme.typography.bodySmall
            )
        }

        PrimaryButton(
            modifier = Modifier
                .constrainAs(okBtn) {
                    top.linkTo(cancelBtn.top)
                    bottom.linkTo(cancelBtn.bottom)
                    start.linkTo(cancelBtn.end)
                    end.linkTo(parent.end, 16.dp)
                    height = Dimension.value(32.dp)
                },
            onClick = {
                coroutine.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onOkClick()
                }
            },
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
        ) {
            Text(
                text = "确定",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SettingItem(
    icon: IconFont,
    text: String,
    onClick: SimpleCallback = { },
    iconStyle: TextStyle = MaterialTheme.typography.titleLarge,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = iconColor
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconFontText(
            icon = icon,
            style = iconStyle,
            color = iconColor
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = text,
            style = titleStyle,
            color = titleColor
        )
    }
}

@Composable
private fun DividerLine() {
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
            .padding(start = 16.dp)
    )
}

@Composable
private fun RankButton(
    icon: IconFont,
    text: String,
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    iconStyle: TextStyle = MaterialTheme.typography.titleLarge,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = iconColor,
    contentStyle: TextStyle = MaterialTheme.typography.bodySmall,
    contentColor: Color = iconColor.copy(alpha = 0.5f),
) {
    CenteredButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ConstraintLayout {
            val (iconText, titleText, contentText) = createRefs()

            createVerticalChain(iconText, titleText, contentText)

            IconFontText(
                modifier = Modifier.constrainAs(iconText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(titleText.top)
                    centerHorizontallyTo(parent)
                },
                icon = icon,
                style = iconStyle,
                color = iconColor
            )

            Text(
                modifier = Modifier.constrainAs(titleText) {
                    centerHorizontallyTo(iconText)
                    top.linkTo(iconText.bottom, 4.dp)
                    bottom.linkTo(contentText.top)
                },
                text = text,
                style = titleStyle,
                color = titleColor
            )

            Text(
                modifier = Modifier.constrainAs(contentText) {
                    centerHorizontallyTo(titleText)
                    top.linkTo(titleText.bottom)
                    bottom.linkTo(parent.bottom, 8.dp)
                },
                text = content,
                style = contentStyle,
                color = contentColor
            )
        }
    }
}

@Composable
@Preview
fun MineScreenPreview() {
    MineScreen(
        vm = viewModel(),
        activity = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TisPreview() {
    LogoutTips(
        rememberModalBottomSheetState(),
        onCancelClick = { },
        onOkClick = { }
    )
}