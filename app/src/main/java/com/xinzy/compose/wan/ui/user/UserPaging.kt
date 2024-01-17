package com.xinzy.compose.wan.ui.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xinzy.compose.wan.entity.Favor
import com.xinzy.compose.wan.entity.Rank
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.http.WanApi
import com.xinzy.compose.wan.http.WanHttpException
import com.xinzy.compose.wan.http.WanRepository

class ScorePagingSource : PagingSource<Int, ScoreRecord>() {

    override fun getRefreshKey(state: PagingState<Int, ScoreRecord>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ScoreRecord> {
        val page = params.key ?: 1
        val result = WanRepository.scoreList(page)

        return if (result.isSuccess) {
            val list = result.data!!
            LoadResult.Page(
                data = list.datas,
                prevKey = list.prevPage,
                nextKey = list.nextPage
            )
        } else {
            LoadResult.Error(WanHttpException(result.code, result.message))
        }
    }
}

class RankPagingSource : PagingSource<Int, Rank>() {
    override fun getRefreshKey(state: PagingState<Int, Rank>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Rank> {
        val page = params.key ?: 1
        val result = WanRepository.rank(page)

        return if (result.isSuccess) {
            val list = result.data!!
            LoadResult.Page(
                data = list.datas,
                prevKey = list.prevPage,
                nextKey = list.nextPage
            )
        } else {
            LoadResult.Error(WanHttpException(result.code, result.message))
        }
    }
}

class FavorPagingSource : PagingSource<Int, Favor>() {
    override fun getRefreshKey(state: PagingState<Int, Favor>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Favor> {
        val page = params.key ?: 0
        val result = WanRepository.collectList(page)

        return if (result.isSuccess) {
            val list = result.data!!
            LoadResult.Page(
                data = list.datas,
                prevKey = list.prevPage,
                nextKey = list.nextPage
            )
        } else {
            LoadResult.Error(WanHttpException(result.code, result.message))
        }
    }
}