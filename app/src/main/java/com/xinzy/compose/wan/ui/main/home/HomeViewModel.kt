package com.xinzy.compose.wan.ui.main.home

import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinzy.compose.wan.util.L
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var viewState by mutableStateOf(HomeState())

    private var isLoading = false

    init {
        dispatch(HomeEvent.Refresh)
    }

    fun dispatch(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadMore -> load()
            HomeEvent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        if (isLoading) return
        isLoading = true
        viewState = viewState.copy(isRefresh = true)

        val articleFlow = flow {
            emit(HomeRepository.article(0))
        }
        val topArticleFlow = flow {
            emit(HomeRepository.top())
        }
        val bannerFlow = flow {
            emit(HomeRepository.banner())
        }

        viewModelScope.launch {
            val allArticleFlow = topArticleFlow.zip(articleFlow) { top, main ->
                top + main
            }
            bannerFlow.zip(allArticleFlow) { banner, articles ->
                viewState = viewState.copy(isRefresh = false, articles = articles, banners = banner)
                isLoading = false
            }.catch {
                viewState = viewState.copy(isRefresh = false)
            }.collect()
        }
    }

    private fun load() {
        if (isLoading) return
        if (HomeRepository.isLoadEnd) return

        viewState = viewState.copy(isLoadingMore = true)

        isLoading = true

        val start = SystemClock.elapsedRealtime()
        viewModelScope.launch {
            val list = HomeRepository.article()
            val delta = SystemClock.elapsedRealtime() - start
            L.d("request article $delta ms")
            if (delta < 1000) {
                delay(1000 - delta)
            }

            val data = viewState.articles + list
            viewState = viewState.copy(articles = data, isLoadingMore = false, isLoadEnd = HomeRepository.isLoadEnd)

            isLoading = false
        }
    }
}