package com.lalosapps.freetoplay.ui.screens.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.getAllGenres
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.usecases.GetFavoritesFlowUseCase
import com.lalosapps.freetoplay.ui.screens.base.ChipData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritesFlowUseCase: GetFavoritesFlowUseCase
) : ViewModel() {

    private var favoriteGenres = listOf<String>()
    private var favoriteGames = listOf<Game>()
    private var genres = listOf<String>()

    private val _favGenres = MutableStateFlow<List<ChipData>>(emptyList())
    val favGenres = _favGenres.asStateFlow()

    private val _favGames = MutableStateFlow<List<Game>>(emptyList())
    val favGames = _favGames.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoritesFlowUseCase()
                .map { it.map { gameDetails -> gameDetails.toGame() } }
                .collect {
                    favoriteGames = it
                    favoriteGenres = it.getAllGenres()
                    genres = emptyList()
                    val curFavGenres = _favGenres.value
                    val updatedFavGenres = favoriteGenres.map { text ->
                        val checked =
                            curFavGenres.find { chip -> chip.text == text }?.checked ?: false
                        if (checked) genres = genres + text
                        ChipData(text, checked)
                    }
                    _favGenres.value = updatedFavGenres
                    filterByGenres()
                }
        }
    }

    var showFilter by mutableStateOf(false)
        private set

    fun onFilterToggle(value: Boolean) {
        _favGenres.value = favoriteGenres.map { ChipData(it, false) }
        showFilter = value
        if (!value) {
            genres = emptyList()
            _favGames.value = favoriteGames
        }
    }

    private fun toggleChipData(text: String) {
        val current = _favGenres.value
        _favGenres.value =
            current.map { if (it.text == text) it.copy(checked = !it.checked) else it }
    }

    fun filterByGenre(genre: String) {
        toggleChipData(genre)
        genres = if (genres.contains(genre)) {
            genres - genre
        } else {
            genres + genre
        }
        var aux = listOf<Game>()
        genres.forEach { g ->
            val list = favoriteGames.filter { it.genre.lowercase() == g.lowercase() }
            list.forEach { game ->
                val found = aux.find { it.id == game.id }
                if (found == null) {
                    aux = aux + game
                } else {
                    aux = aux - found
                    aux = aux + game
                }
            }
        }
        if (aux.isNotEmpty()) {
            _favGames.value = aux
        } else {
            if (genres.isNotEmpty()) {
                // should not happen in this case since we make sure we get genres from the existing favorite games
                // if we display all genres like in search screen then this would apply
                _favGames.value = emptyList()
            } else {
                _favGames.value = favoriteGames
            }
        }
    }

    private fun filterByGenres() {
        if (genres.isEmpty()) {
            _favGames.value = favoriteGames
        } else {
            var aux = listOf<Game>()
            genres.forEach { g ->
                val list = favoriteGames.filter { it.genre.lowercase() == g.lowercase() }
                list.forEach { game ->
                    val found = aux.find { it.id == game.id }
                    if (found == null) {
                        aux = aux + game
                    } else {
                        aux = aux - found
                        aux = aux + game
                    }
                }
            }
            _favGames.value = aux
        }
    }
}