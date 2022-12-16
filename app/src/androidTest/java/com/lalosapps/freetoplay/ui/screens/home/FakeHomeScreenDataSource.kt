package com.lalosapps.freetoplay.ui.screens.home

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource

object FakeHomeScreenDataSource {

    val games = FakeFreeToPlayAppDataSource.games

    val manyGames = games + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 3",
        gameUrl = "Game Url 3",
        genre = "Card Game",
        id = 3,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Turn-based card game with heavy focus on control decks.",
        thumbnail = "Thumbnail 3",
        title = "Titans 3",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 4",
        gameUrl = "Game Url 4",
        genre = "Card Game",
        id = 4,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Turn-based card game with heavy focus on control decks.",
        thumbnail = "Thumbnail 4",
        title = "Titans 4",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 5",
        gameUrl = "Game Url 5",
        genre = "Card Game",
        id = 5,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Turn-based card game with heavy focus on control decks.",
        thumbnail = "Thumbnail 5",
        title = "Titans 5",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 6",
        gameUrl = "Game Url 6",
        genre = "Card Game",
        id = 6,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Turn-based card game with heavy focus on control decks.",
        thumbnail = "Thumbnail 6",
        title = "Titans 6",
        isFavorite = false
    )

    val emptyGames = emptyList<Game>()

    val uiStateSuccess = Resource.Success(data = games)

    val uiStateErrorWithThrowable = Resource.Error<List<Game>>(error = Throwable("API Error!"))

    val uiStateErrorPlain = Resource.Error<List<Game>>()

}