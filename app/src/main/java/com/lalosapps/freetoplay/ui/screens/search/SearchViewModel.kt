package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.getAllGenres
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.usecases.GetGamesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getGamesFlowUseCase: GetGamesFlowUseCase
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var showSearch by mutableStateOf(true)
        private set

    var showFilter by mutableStateOf(false)
        private set

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games = _games.asStateFlow()

    private val _allGenres = MutableStateFlow<List<ChipData>>(emptyList())
    val allGenres = _allGenres.asStateFlow()

    private var originalGenres = listOf<String>()

    init {
        viewModelScope.launch {
            getGamesFlowUseCase()
                .map { it.getAllGenres() }
                .collect {
                    originalGenres = it
                }
        }
    }

    var originalList = listOf<Game>()
        private set

    private var genres = listOf<String>()

    fun onQueryChange(query: String) {
        this.query = query
        showSearch = query.isEmpty()
        filterByGenresWithQuery(query)
    }

    fun onQueryClear() {
        this.query = ""
        showSearch = true
        filterByGenresWithQuery(query)
    }

    fun setGames(list: List<Game>) {
        originalList = list
        if (query.isEmpty() && genres.isEmpty()) {
            _games.value = list
        } else {
            filterByGenresWithQuery(query)
        }
    }

    fun showSearchResults() {
        showSearch = true
    }

    fun onFilterToggle(value: Boolean) {
        _allGenres.value = originalGenres.map { ChipData(it, false) }
        showFilter = value
        if (!value) {
            genres = emptyList()
            _games.value = originalList.filter { it.title.lowercase().contains(query.lowercase()) }
        }
    }

    private fun toggleChipData(text: String) {
        val current = _allGenres.value
        _allGenres.value =
            current.map { if (it.text == text) it.copy(checked = !it.checked) else it }
    }

    fun filterByGenre(genre: String) {
        toggleChipData(genre)
        genres = if (genres.contains(genre)) {
            genres - genre
        } else {
            genres + genre
        }
        val aux = mutableListOf<Game>()
        genres.forEach { g ->
            val list = originalList.filter { it.genre.lowercase() == g.lowercase() }
            aux.addAll(list)
        }
        if (aux.isNotEmpty()) {
            val filtered = aux.toList().filter { it.title.lowercase().contains(query.lowercase()) }
            _games.value = filtered
        } else {
            if (genres.isNotEmpty()) {
                _games.value = emptyList()
            } else {
                val filtered =
                    originalList.filter { it.title.lowercase().contains(query.lowercase()) }
                _games.value = filtered
            }
        }
    }

    private fun filterByGenresWithQuery(query: String) {
        if (genres.isEmpty()) {
            _games.value =
                originalList.filter { it.title.lowercase().contains(query.lowercase()) }
        } else {
            val aux = mutableListOf<Game>()
            genres.forEach { g ->
                val list = originalList.filter { it.genre.lowercase() == g.lowercase() }
                aux.addAll(list)
            }
            _games.value = aux.toList().filter { it.title.lowercase().contains(query.lowercase()) }
        }
    }
}