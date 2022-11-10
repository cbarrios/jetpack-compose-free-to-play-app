package com.lalosapps.freetoplay.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import com.lalosapps.freetoplay.domain.usecases.GetGamesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GamesRepository,
    getGamesFlowUseCase: GetGamesFlowUseCase
) : ViewModel() {

    var splashScreenVisible by mutableStateOf(true)
        private set

    private val _uiState = MutableStateFlow<Resource<List<Game>>>(Resource.Loading)
    val uiState = _uiState.asStateFlow()

    private val _gamesList = MutableStateFlow<List<Game>>(emptyList())
    val gamesList = _gamesList.asStateFlow()

    private var originalList = listOf<Game>()

    private val gamesFlow = getGamesFlowUseCase()

    init {
        viewModelScope.launch {
            val result = repository.getAllGames()
            _uiState.value = result
            gamesFlow.collect {
                _gamesList.value = it
                originalList = it
                if (splashScreenVisible) splashScreenVisible = false
            }
        }
    }

    fun setAllGames() {
        if (originalList.isNotEmpty()) {
            _gamesList.value = originalList
        }
    }

    fun setPcGames() {
        if (originalList.isNotEmpty()) {
            _gamesList.value =
                originalList.filter { it.platform.contains("windows", ignoreCase = true) }
        }
    }

    fun setWebGames() {
        if (originalList.isNotEmpty()) {
            _gamesList.value =
                originalList.filter { !it.platform.contains("windows", ignoreCase = true) }
        }
    }

    fun setLatestGames() {
        if (originalList.isNotEmpty()) {
            _gamesList.value = originalList.sortedByDescending { it.releaseDate }
        }
    }
}