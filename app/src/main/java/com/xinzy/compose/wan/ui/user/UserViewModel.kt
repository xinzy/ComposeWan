package com.xinzy.compose.wan.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinzy.compose.wan.WanApplication
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.entity.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    var userState by mutableStateOf(UserState<Nothing>())

    var scoreState by mutableStateOf(UserState<Score>())
    var scoreRecordState by mutableStateOf(UserState<List<ScoreRecord>>())

    fun dispatch(event: UserEvent) {
        when (event) {
            is UserEvent.Login -> login(event.username, event.password)
            is UserEvent.Register -> register(event.username, event.password, event.confirm)
            is UserEvent.Logout -> logout()
            is UserEvent.Score -> score()
        }
    }

    private fun login(username: String, password: String) {
        if (username.isEmpty()) {
            userState = userState.copy(message = "请输入用户名")
            return
        }
        if (password.isEmpty()) {
            userState = userState.copy(message = "请输入密码")
            return
        }
        userState = userState.copy(loading = true, message = "")
        viewModelScope.launch {
            val result = UserRepository.login(username, password)
            result.first?.let {
                User.me().login(it)
                User.me().save()
                userState = userState.copy(loading = false, success = true, message = result.second)
            } ?: run {
                userState = userState.copy(loading = false, message = result.second)
            }
        }
    }

    private fun register(username: String, password: String, confirm: String) {
        if (username.isEmpty()) {
            userState = userState.copy(message = "请输入用户名")
            return
        }
        if (password.isEmpty()) {
            userState = userState.copy(message = "请输入密码")
            return
        }
        if (password != confirm) {
            userState = userState.copy(message = "两次密码不一致")
            return
        }
        userState = userState.copy(loading = true, message = "")
        viewModelScope.launch {
            val result = UserRepository.register(username, password, password)
            result.first?.let {
                User.me().login(it)
                User.me().save()
                userState = userState.copy(loading = false, success = true, message = result.second)
            } ?: run {
                userState = userState.copy(loading = false, message = result.second)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            UserRepository.logout()
            User.me().logout()
        }
    }

    private fun score() {
        viewModelScope.launch {
            val score = UserRepository.score()
            scoreState = scoreState.copy(data = score)
        }
    }
}