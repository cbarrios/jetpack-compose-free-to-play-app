package com.lalosapps.freetoplay.ui.screens.game_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import com.lalosapps.freetoplay.domain.usecases.GetGameDetailsFlowUseCase
import com.lalosapps.freetoplay.ui.screens.base.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    savedStateHandle: SavedStateHandle,
    getGameDetailsFlowUseCase: GetGameDetailsFlowUseCase
) : ViewModel() {

    private val _game = MutableStateFlow<Resource<GameDetails>>(Resource.Loading)
    val game = _game.asStateFlow()

    init {
        savedStateHandle.get<Int>(key = Screen.GameDetails.A1_INT_GAME_ID)?.let { gameId ->
            viewModelScope.launch {
                val result = gamesRepository.getGame(gameId)
                getGameDetailsFlowUseCase(gameId).collect {
                    if (it.isEmpty()) {
                        _game.value = result
                    } else {
                        _game.value = Resource.Success(it.first())
                    }
                }
            }
        }
    }
}