package com.lalosapps.freetoplay.ui.main.fake

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game

object FakeFreeToPlayAppDataSource {

    val games = listOf(
        Game(
            developer = "XYZ Devs",
            freeToGameProfileUrl = "Profile Url 1",
            gameUrl = "Game Url 1",
            genre = "Card Game",
            id = 1,
            platform = "Windows",
            publisher = "XYZ Inc",
            releaseDate = "2022-11-17",
            shortDescription = "Turn-based card game with focus on control decks.",
            thumbnail = "Thumbnail 1",
            title = "Titans",
            isFavorite = false
        )
    )

    val uiState: Resource<List<Game>> = Resource.Success(data = games)
}