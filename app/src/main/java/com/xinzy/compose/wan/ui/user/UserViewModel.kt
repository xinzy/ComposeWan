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

    var userState: UserState by mutableStateOf(UserState.Default)

    var scoreState by mutableStateOf(UserState.Success(Score()))

    var scoreRecordState: UserState by mutableStateOf(UserState.Default)


    val scoreRecords = mutableListOf<ScoreRecord>()
    var isScoreRecordLoadEnd = false
    private var scoreRecordPage = 1
    private var isLoadingScoreRecord = false

    fun dispatch(event: UserEvent) {
        when (event) {
            is UserEvent.Login -> login(event.username, event.password)
            is UserEvent.Register -> register(event.username, event.password, event.confirm)
            UserEvent.Logout -> logout()
            UserEvent.Score -> score()
            UserEvent.ScoreRecordRefresh -> refreshScoreRecord()
            UserEvent.ScoreRecord -> scoreRecord()
        }
    }

    private fun login(username: String, password: String) {
        if (username.isEmpty()) {
            userState = UserState.Failure(message = "请输入用户名")
            return
        }
        if (password.isEmpty()) {
            userState = UserState.Failure(message = "请输入密码")
            return
        }
        userState = UserState.Loading
        viewModelScope.launch {
            val result = UserRepository.login(username, password)
            result.first?.let {
                User.me().login(it)
                User.me().save()
                userState = UserState.Success(data = result.second)
            } ?: run {
                userState = UserState.Failure(message = result.second)
            }
        }
    }

    private fun register(username: String, password: String, confirm: String) {
        if (username.isEmpty()) {
            userState = UserState.Failure(message = "请输入用户名")
            return
        }
        if (password.isEmpty()) {
            userState = UserState.Failure(message = "请输入密码")
            return
        }
        if (password != confirm) {
            userState = UserState.Failure(message = "两次密码不一致")
            return
        }
        userState = UserState.Loading
        viewModelScope.launch {
            val result = UserRepository.register(username, password, password)
            result.first?.let {
                User.me().login(it)
                User.me().save()
                userState = UserState.Success(data = result.second)
            } ?: run {
                userState = UserState.Failure(message = result.second)
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
            score?.let {
                scoreState = UserState.Success(data = it)
            }
        }
    }

    private fun refreshScoreRecord() {
        isScoreRecordLoadEnd = false
        scoreRecordPage = 1
        scoreRecord()
    }

    private fun scoreRecord() {
        if (isScoreRecordLoadEnd) {
            scoreRecordState = UserState.Failure("已加载全部数据")
            return
        }
        if (isLoadingScoreRecord) return
        isLoadingScoreRecord = true

        scoreRecordState = if (scoreRecordPage == 1) UserState.Loading else UserState.LoadMore

        viewModelScope.launch {
            val result = UserRepository.scoreList(scoreRecordPage)
            scoreRecordState = if (result.isSuccess) {
                val list = result.data!!
                if (scoreRecordPage == 1) {
                    scoreRecords.clear()
                }
                scoreRecords.addAll(list.getData())
                isScoreRecordLoadEnd = list.over
                scoreRecordPage = list.page
                UserState.Success(scoreRecords)
            } else {
                UserState.Failure(result.message)
            }
            isLoadingScoreRecord = false
        }
    }
}