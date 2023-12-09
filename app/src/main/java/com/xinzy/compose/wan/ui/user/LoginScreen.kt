package com.xinzy.compose.wan.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.widget.WanTextField

@Composable
fun LoginScreen(
    vm: UserViewModel,
    activity: UserActivity?,
    modifier: Modifier = Modifier,
    onChangeType: ((Int) -> Unit) = { }
) {
    val userState = vm.userState

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(true) }

    if (userState.success) {
        activity?.finish()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 96.dp,
                start = 20.dp,
                end = 20.dp
            )
            .verticalScroll(rememberScrollState()),
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "",
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        WanTextField(
            value = username,
            onValueChange = {
                username = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = "输入用户名",
            leadingIcon = R.string.icon_username,
            trailingIcon = R.string.icon_close,
            onTrailingClick = {
                username = ""
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    this.defaultKeyboardAction(ImeAction.Next)
                }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        WanTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = "输入密码",
            leadingIcon = R.string.icon_password,
            trailingIcon = if (showPassword) R.string.icon_password_show else R.string.icon_password_hide,
            onTrailingClick = {
                showPassword = !showPassword
            },
            visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardActions = KeyboardActions(
                onDone = {
                    this.defaultKeyboardAction(ImeAction.Go)
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        if (!userState.success && userState.message.isNotEmpty()) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .padding(end = 24.dp)
            ) {
                Text(
                    text = userState.message,
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(72.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .width(196.dp)
                    .height(48.dp),
                onClick = {
                    vm.dispatch(UserEvent.Login(username, password))
                }
            ) {
                if (userState.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.clickable {
                    onChangeType.invoke(UserActivity.TYPE_REGISTER)
                },
                text = "没有账号? 注册一个吧",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen(
        vm = viewModel(),
        activity = null,
        modifier = Modifier.width(200.dp)
    )
}