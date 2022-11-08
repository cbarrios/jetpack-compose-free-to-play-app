package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lalosapps.freetoplay.domain.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var query by mutableStateOf("")
        private set

    var showSearch by mutableStateOf(true)
        private set

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games = _games.asStateFlow()

    var originalList = listOf<Game>()
        private set

    fun onQueryChange(query: String) {
        this.query = query
        showSearch = query.isEmpty()
        _games.value = originalList.filter { it.title.lowercase().contains(query.lowercase()) }
    }

    fun onQueryClear() {
        this.query = ""
        showSearch = true
        _games.value = originalList
    }

    fun setGames(list: List<Game>) {
        _games.value = list
        originalList = list
    }

    fun showSearchResults() {
        showSearch = true
    }
}