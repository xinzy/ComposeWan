package com.xinzy.compose.wan.ui.user

import com.xinzy.compose.wan.entity.Favor
import java.lang.IllegalStateException

sealed class UserState {

    val isSuccess: Boolean get() = this is Success<*>
    val isLoading: Boolean get() = this == Loading
    val isLoadMore: Boolean get() = this == LoadMore
    val isFailure: Boolean get() = this is Failure

    val successData: Any get() = if (this is Success<*>) this.data!! else throw IllegalStateException("$this is not Success")
    val errorMessage: String get() = if (this is Failure) this.message else throw IllegalStateException("$this is not Failure")


    object Default : UserState()

    object Loading : UserState()

    object LoadMore: UserState()

    data class Success<T>(
        val data: T
    ) : UserState()

    data class Failure(
        val message: String
    ) : UserState()
}

data class FavorResult(
    val success: Boolean,
    val favor: Favor,
    val message: String
)
