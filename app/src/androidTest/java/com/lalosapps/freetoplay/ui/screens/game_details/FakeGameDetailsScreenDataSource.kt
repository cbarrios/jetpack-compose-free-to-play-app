package com.lalosapps.freetoplay.ui.screens.game_details

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource
import kotlinx.coroutines.flow.MutableStateFlow

object FakeGameDetailsScreenDataSource {

    val uiStateLoading: Resource<GameDetails> = Resource.Loading

    val uiStateError: Resource.Error<GameDetails> =
        Resource.Error(error = Throwable("Error: Couldn't fetch game details."))

    val gameDetailsDefault = FakeFreeToPlayAppDataSource.gameDetails

    var uiStateSuccess: Resource.Success<GameDetails> = Resource.Success(data = gameDetailsDefault)
        private set(value) {
            uiStateFlowSuccess.value = value
            field = value
        }

    val uiStateFlowSuccess = MutableStateFlow(uiStateSuccess)

    fun setUiStateSuccessData(gameDetails: GameDetails) {
        uiStateSuccess = Resource.Success(data = gameDetails)
    }

    fun toggleFavorite() {
        val aux = uiStateFlowSuccess.value.data
        val copy = aux.copy(isFavorite = !aux.isFavorite)
        setUiStateSuccessData(copy)
    }
}