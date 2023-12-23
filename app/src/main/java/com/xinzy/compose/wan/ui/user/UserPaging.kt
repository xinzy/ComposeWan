package com.xinzy.compose.wan.ui.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.http.WanHttpException

class ScorePagingSource : PagingSource<Int, ScoreRecord>() {

    override fun getRefreshKey(state: PagingState<Int, ScoreRecord>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ScoreRecord> {
        val page = params.key ?: 1
        val result = UserRepository.scoreList(page)

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