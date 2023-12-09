package com.xinzy.compose.wan.ui.main.chapter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChapterViewModel : ViewModel() {

    var viewState by mutableStateOf(ChapterState())

    init {
        dispatch(ChapterEvent.Load)
    }

    fun dispatch(event: ChapterEvent) {
        when (event) {
            ChapterEvent.Load -> load()
        }
    }

    private fun load() {
        viewState = viewState.copy(refreshing = true)
        viewModelScope.launch {
            val list = ChapterRepository.chapters()
            viewState = viewState.copy(refreshing = false, chapters = list)
        }
    }
}