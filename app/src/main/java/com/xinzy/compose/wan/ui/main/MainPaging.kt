package com.xinzy.compose.wan.ui.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.http.toException


class HomePagingSource : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 0

        val top = if (page == 0) {
            MainRepository.top()
        } else {
            null
        }
        val homeArticle = MainRepository.homeArticle(page)
        return if (homeArticle.isSuccess) {
            val list = homeArticle.data!!
            LoadResult.Page(
                data = if (top == null) list.datas else top + list.datas,
                prevKey = if (list.isFirstPage) null else page - 1,
                nextKey = if (list.isLastPage) null else list.page
            )
        } else {
            if (top == null) {
                LoadResult.Error(homeArticle.toException())
            } else {
                LoadResult.Page(
                    data = top,
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }
}

class WechatPagingSource(
    private val cid: Int,
    var keyword: String = ""
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        val result = MainRepository.wechatArticle(cid, page, keyword)

        return if (result.isSuccess) {
            val list = result.data!!
            LoadResult.Page(
                data = list.datas,
                prevKey = list.prevPage,
                nextKey = list.nextPage
            )
        } else {
            LoadResult.Error(result.toException())
        }
    }
}

class ProjectPagingSource(private val cid: Int) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        val result = MainRepository.projectArticle(cid, page)
        return if (result.isSuccess) {
            val list = result.data!!
            LoadResult.Page(
                data = list.datas,
                prevKey = list.prevPage,
                nextKey = list.nextPage
            )
        } else {
            LoadResult.Error(result.toException())
        }
    }

}