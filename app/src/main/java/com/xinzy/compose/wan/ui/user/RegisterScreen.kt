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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xinzy.compose.wan.R
import com.xinzy.compose.wan.ui.widget.ShowToast
import com.xinzy.compose.wan.ui.widget.WanTextField
import com.xinzy.compose.wan.util.IconFont

@Composable
fun RegisterScreen(
    vm: UserViewModel,
    activity: UserActivity?,
    modifier: Modifier = Modifier,
    onChangeType: ((UserUiType) -> Unit) = { }
) {
    val userState = vm.userState

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(true) }
    var showConfirmPassword by remember { mutableStateOf(true) }

    if (userState.success) {
        ShowToast(msg = userState.message)
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
            leadingIcon = IconFont.Username,
            trailingIcon = IconFont.Close,
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
            leadingIcon = IconFont.Password,
            trailingIcon = if (showPassword) IconFont.PasswordShow else IconFont.PasswordHide,
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

        Spacer(modifier = Modifier.height(24.dp))

        WanTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = "确认密码",
            leadingIcon = IconFont.Password,
            trailingIcon = if (showConfirmPassword) IconFont.PasswordShow else IconFont.PasswordHide,
            onTrailingClick = {
                showConfirmPassword = !showConfirmPassword
            },
            visualTransformation = if (showConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Go)
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        if (userState.message.isNotEmpty()) {

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
                    vm.dispatch(UserEvent.Register(username, password, confirmPassword))
                }
            ) {
                if (userState.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "注册",
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
                    onChangeType.invoke(UserUiType.Login)
                },
                text = "已有账号? 去登录",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun RegisterScreenPreview() {
    RegisterScreen(
        vm = viewModel(),
        activity = null,
        modifier = Modifier.width(200.dp)
    )
}