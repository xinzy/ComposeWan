package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.ui.widget.IconFontText
import com.xinzy.compose.wan.ui.widget.IconTextButton
import com.xinzy.compose.wan.util.IconFont

@Composable
fun MinScreen(
    vm: UserViewModel,
    activity: UserActivity?,
    modifier: Modifier = Modifier
) {
    var showLogoutConfirm by remember {
        mutableStateOf(false)
    }

    MineContent(
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun MineContent(
    modifier: Modifier = Modifier
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
                .height(56.dp)
                .padding(horizontal = 48.dp)
        ) {

            IconTextButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = IconFont.Rank,
                text = "排行",
                onClick = {

                }
            )

            IconTextButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = IconFont.Score,
                text = "积分",
                onClick = {

                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {

            SettingItem(
                icon = IconFont.MyFavor,
                text = "我的收藏"
            )

            Spacer(
                modifier = Modifier.height(1.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
                    .padding(start = 16.dp)
            )

            SettingItem(
                icon = IconFont.Message,
                text = "我的消息"
            )

            Spacer(
                modifier = Modifier.height(1.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
                    .padding(start = 16.dp)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            SettingItem(
                icon = IconFont.Logout,
                text = "退出登录",
                onClick = {
                }
            )

        }
    }
}


@Composable
private fun SettingItem(
    icon: IconFont,
    text: String,
    onClick: () -> Unit = { },
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
@Preview
fun MineScreenPreview() {
    MinScreen(
        vm = viewModel(),
        activity = null
    )
}