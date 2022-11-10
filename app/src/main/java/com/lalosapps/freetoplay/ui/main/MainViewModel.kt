package com.lalosapps.freetoplay.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import com.lalosapps.freetoplay.domain.usecases.GetFavoritesFlowUseCase
import com.lalosapps.freetoplay.domain.usecases.GetGamesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GamesRepository,
    getGamesFlowUseCase: GetGamesFlowUseCase,
    getFavoritesFlowUseCase: GetFavoritesFlowUseCase
) : ViewModel() {

    var splashScreenVisible by mutableStateOf(true)
        private set

    private val _uiState = MutableStateFlow<Resource<List<Game>>>(Resource.Loading)
    val uiState = _uiState.asStateFlow()

    private val _gamesList = MutableStateFlow<List<Game>>(emptyList())
    val gamesList = _gamesList.asStateFlow()

    private var originalList = listOf<Game>()

    private var gameType: GameType = GameType.All

    private val gamesFlow =
        combine(getGamesFlowUseCase(), getFavoritesFlowUseCase()) { games, gamesDetails ->
            games.map {
                val isFavorite =
                    gamesDetails.find { gameDetails -> gameDetails.id == it.id }?.isFavorite
                        ?: false
                it.copy(isFavorite = isFavorite)
            }
        }

    init {
        viewModelScope.launch {
            val result = repository.getAllGames()
            _uiState.value = result
            gamesFlow.collect {
                originalList = it
                when (gameType) {
                    GameType.All -> setAllGames()
                    GameType.Pc -> setPcGames()
                    GameType.Web -> setWebGames()
                    GameType.Latest -> setLatestGames()
                }
                if (splashScreenVisible) splashScreenVisible = false
            }
        }
    }

    fun setAllGames() {
        if (originalList.isNotEmpty()) {
            gameType = GameType.All
            _gamesList.value = originalList
        }
    }

    fun setPcGames() {
        if (originalList.isNotEmpty()) {
            gameType = GameType.Pc
            _gamesList.value =
                originalList.filter { it.platform.contains("windows", ignoreCase = true) }
        }
    }

    fun setWebGames() {
        if (originalList.isNotEmpty()) {
            gameType = GameType.Web
            _gamesList.value =
                originalList.filter { !it.platform.contains("windows", ignoreCase = true) }
        }
    }

    fun setLatestGames() {
        if (originalList.isNotEmpty()) {
            gameType = GameType.Latest
            _gamesList.value = originalList.sortedByDescending { it.releaseDate }
        }
    }

    private enum class GameType {
        All, Pc, Web, Latest
    }
}