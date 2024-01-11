package com.xinzy.compose.wan.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.Navigation
import com.xinzy.compose.wan.http.WanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    /** 首页的分页信息*/
    val homeArticle = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { HomePagingSource() }
    ).flow.cachedIn(viewModelScope)

    /** 首页状态 */
    var homeViewState by mutableStateOf(HomeState())

    var homeCollectState: FavorState? by mutableStateOf(null)

    /** 分类数据 */
    var chapterViewState by mutableStateOf(SingleState<List<Chapter>>(data = emptyList()))

    /** 导航数据 */
    var navigationViewState by mutableStateOf(SingleState<List<Navigation>>(data = emptyList()))

    /** 微信分类数据 */
    var wechatViewState by mutableStateOf(SingleState<List<Chapter>>(data = emptyList()))

    var wechatCollectState: FavorState? by mutableStateOf(null)

    private val wechatKeywords = hashMapOf<Int, String>()
    fun getWechatKeyword(cid: Int): String = wechatKeywords[cid] ?: ""
    private fun getWechatPagingSource(cid: Int): WechatPagingSource = WechatPagingSource(cid, wechatKeywords[cid] ?: "")

    private val wechatPagers = hashMapOf<Int, Flow<PagingData<Article>>>()
    fun getWechatPager(cid: Int): Flow<PagingData<Article>> = wechatPagers[cid] ?: Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = { getWechatPagingSource(cid) })
        .flow
        .cachedIn(viewModelScope)
        .also { wechatPagers[cid] = it }

    /** 项目分类数据 */
    var projectViewState by mutableStateOf(SingleState<List<Chapter>>(data = emptyList()))
    var projectCollectState: FavorState? by mutableStateOf(null)

    private val projectPagers = hashMapOf<Int, Flow<PagingData<Article>>>()
    fun getProjectPager(cid: Int): Flow<PagingData<Article>> = projectPagers[cid] ?: Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = { ProjectPagingSource(cid) })
        .flow
        .cachedIn(viewModelScope)
        .also { projectPagers[cid] = it }

    fun dispatch(event: MainEvent) {
        when (event) {
            MainEvent.LoadBanner -> loadBanner()
            MainEvent.LoadChapter -> loadChapter()
            MainEvent.LoadNavigationChapter -> loadNav()
            MainEvent.LoadWechatChapter -> loadWechatChapter()
            is MainEvent.UpdateWechatKeyword -> updateWechatKeyword(event)
            MainEvent.LoadProjectChapter -> loadProjectChapter()
            is MainEvent.Collect -> collect(event)
        }
    }

    private fun loadBanner() {
        if (homeViewState.isLoaded) return
        viewModelScope.launch {
            val banner = WanRepository.banner()
            homeViewState = homeViewState.copy(isLoaded = banner.isNotEmpty(), banners = banner)
        }
    }

    private fun loadChapter() {
        if (chapterViewState.isLoaded) return

        chapterViewState = chapterViewState.copy(refreshing = true)
        viewModelScope.launch {
            val list = WanRepository.chapters()
            chapterViewState = chapterViewState.copy(isLoaded = list.isNotEmpty(), refreshing = false, data = list)
        }
    }

    private fun loadNav() {
        if (navigationViewState.isLoaded) return

        navigationViewState = navigationViewState.copy(refreshing = true)
        viewModelScope.launch {
            val list = WanRepository.navigation()
            navigationViewState = navigationViewState.copy(isLoaded = list.isNotEmpty(), refreshing = false, data = list)
        }
    }

    private fun loadWechatChapter() {
        if (wechatViewState.isLoaded) return

        wechatViewState = wechatViewState.copy(refreshing = true)
        viewModelScope.launch {
            val list = WanRepository.wechat()
            wechatViewState = wechatViewState.copy(isLoaded = list.isNotEmpty(), refreshing = false, data = list)
        }
    }

    private fun updateWechatKeyword(data: MainEvent.UpdateWechatKeyword) {
        wechatKeywords[data.cid] = data.keyword
    }

    private fun loadProjectChapter() {
        if (projectViewState.isLoaded) return

        projectViewState = projectViewState.copy(refreshing = true)
        viewModelScope.launch {
            val list = WanRepository.project()
            projectViewState = projectViewState.copy(isLoaded = list.isNotEmpty(), refreshing = false, data = list)
        }
    }

    private fun collect(data: MainEvent.Collect) {
        viewModelScope.launch {
            val result = if (data.collect) {
                WanRepository.collect(data.article.id)
            } else {
                WanRepository.uncollectOrigin(data.article.id)
            }
            val state = if (result.isSuccess) {
                data.article.collect = data.collect
                FavorState(
                    article = data.article,
                    collect = data.collect,
                    success = true,
                    message = "${if (data.collect) "收藏" else "取消收藏" }成功"
                )
            } else {
                FavorState(
                    article = data.article,
                    collect = data.collect,
                    success = false,
                    message = result.message
                )
            }

            when (data.source) {
                CollectSource.Home -> {
                    homeCollectState = state
                }
                CollectSource.Wechat -> {
                    wechatCollectState = state
                }
                CollectSource.Project -> {
                    projectCollectState = state
                }
            }
        }
    }
}