package com.lalosapps.freetoplay.ui.screens.game_details

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource

object FakeGameDetailsScreenDataSource {

    val uiStateLoading: Resource<GameDetails> = Resource.Loading

    val uiStateError: Resource.Error<GameDetails> =
        Resource.Error(error = Throwable("Error: Couldn't fetch game details."))

    val gameDetailsDefault = FakeFreeToPlayAppDataSource.gameDetails

    var uiStateSuccess: Resource.Success<GameDetails> = Resource.Success(data = gameDetailsDefault)
        private set

    fun setUiStateSuccessData(gameDetails: GameDetails) {
        uiStateSuccess = Resource.Success(data = gameDetails)
    }
}