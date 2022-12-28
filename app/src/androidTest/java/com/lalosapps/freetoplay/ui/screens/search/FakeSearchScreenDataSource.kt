package com.lalosapps.freetoplay.ui.screens.search

import com.lalosapps.freetoplay.core.util.getAllGenres
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource

object FakeSearchScreenDataSource {

    val manyGames = FakeFreeToPlayAppDataSource.games + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 3",
        gameUrl = "Game Url 3",
        genre = "MMORPG",
        id = 3,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "An online role-playing video game in which a very large number of people participate simultaneously.",
        thumbnail = "Thumbnail 3",
        title = "Titans 3",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 4",
        gameUrl = "Game Url 4",
        genre = "Strategy",
        id = 4,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Strategy game.",
        thumbnail = "Thumbnail 4",
        title = "Titans 4",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 5",
        gameUrl = "Game Url 5",
        genre = "MOBA",
        id = 5,
        platform = "Web",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "MOBA game.",
        thumbnail = "Thumbnail 5",
        title = "Titans 5",
        isFavorite = false
    ) + Game(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 6",
        gameUrl = "Game Url 6",
        genre = "Sports",
        id = 6,
        platform = "Windows",
        publisher = "XYZ Inc",
        releaseDate = "2022-12-15",
        shortDescription = "Soccer game.",
        thumbnail = "Thumbnail 6",
        title = "Titans 6",
        isFavorite = false
    )

    val allGenres = manyGames.getAllGenres()

    val strategyGames = manyGames.filter { it.genre == "Strategy" }
    val cardGames = manyGames.filter { it.genre == "Card Game" }
    val sportsGames = manyGames.filter { it.genre == "Sports" }

    val emptyGames = emptyList<Game>()

    const val topBarTitle = "Free To Play"
}