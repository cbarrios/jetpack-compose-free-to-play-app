package com.lalosapps.freetoplay.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lalosapps.freetoplay.domain.usecases.GetFavoritesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritesFlowUseCase: GetFavoritesFlowUseCase
) : ViewModel() {

    val favorites =
        getFavoritesFlowUseCase()
            .map { it.map { gameDetails -> gameDetails.toGame() } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}