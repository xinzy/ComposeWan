package com.xinzy.compose.wan.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.xinzy.compose.wan.entity.Favor
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.http.WanRepository
import com.xinzy.compose.wan.util.L
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    var userState: UserState by mutableStateOf(UserState.Default)

    var scoreState by mutableStateOf(UserState.Success(Score()))

    val scoreRecordPager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ScorePagingSource() }
    ).flow.cachedIn(viewModelScope)

    val rankPager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { RankPagingSource() }
    ).flow.cachedIn(viewModelScope)

    val favorPager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { FavorPagingSource() }
    ).flow.cachedIn(viewModelScope)

    private var favorResult: FavorResult? by mutableStateOf(null)
    fun lastFavorResult(): FavorResult? {
        val result = favorResult ?: return null
        favorResult = null
        return result
    }

    fun dispatch(event: UserEvent) {
        when (event) {
            is UserEvent.Login -> login(event.username, event.password)
            is UserEvent.Register -> register(event.username, event.password, event.confirm)
            UserEvent.Logout -> logout()
            UserEvent.Score -> score()
            is UserEvent.UnCollect -> unCollect(event.favor)
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
            val result = WanRepository.login(username, password)
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
            val result = WanRepository.register(username, password, password)
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
            WanRepository.logout()
            User.me().logout()
        }
    }

    private fun score() {
        viewModelScope.launch {
            val score = WanRepository.score()
            score?.let {
                scoreState = UserState.Success(data = it)
            }
        }
    }

    private fun unCollect(favor: Favor) {
        viewModelScope.launch {
            val result = WanRepository.uncollect(favor.id, favor.originId ?: -1)

            favorResult = if (result.isSuccess) {
                FavorResult(true, favor, "取消收藏成功")
            } else {
                FavorResult(false, favor, "取消收藏失败")
            }
            L.d("UnCollect: ${result.isSuccess}, $favorResult")
        }
    }
}