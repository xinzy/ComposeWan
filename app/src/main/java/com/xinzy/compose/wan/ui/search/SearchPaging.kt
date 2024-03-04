package com.xinzy.compose.wan.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.http.WanRepository
import com.xinzy.compose.wan.http.toException

class SearchPagingSource(private val keyword: String) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 0
        val result = WanRepository.search(page, keyword)

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