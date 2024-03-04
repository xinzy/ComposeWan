package com.xinzy.compose.wan.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.xinzy.compose.wan.http.WanRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var keyword: String = ""

    var searchState: SearchState by mutableStateOf(SearchState())

    var searchPaging = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchPagingSource(keyword) }
        )
        .flow
        .cachedIn(viewModelScope)

    fun dispatch(event: SearchEvent) {
        when (event) {
            SearchEvent.LoadingHotkey -> loadHotkey()
            is SearchEvent.Search -> search(event.keyword)
        }
    }


    private fun loadHotkey() {
        viewModelScope.launch {
            val hotkeys = WanRepository.hotkey()
            searchState = searchState.copy(hotkeys = hotkeys)
        }
    }

    private fun search(keyword: String) {
        this.keyword = keyword
        searchState = searchState.copy(inSearching = true, keyword = keyword)
    }
}