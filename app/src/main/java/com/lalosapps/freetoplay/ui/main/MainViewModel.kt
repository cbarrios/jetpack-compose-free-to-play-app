package com.lalosapps.freetoplay.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GamesRepository
) : ViewModel() {

    var splashScreenVisible by mutableStateOf(true)
        private set

    private val _games = MutableStateFlow<Resource<List<Game>>>(Resource.Loading)
    val games = _games.asStateFlow()

    init {
        viewModelScope.launch {
            _games.value = repository.getAllGames()
            splashScreenVisible = false
        }
    }
}