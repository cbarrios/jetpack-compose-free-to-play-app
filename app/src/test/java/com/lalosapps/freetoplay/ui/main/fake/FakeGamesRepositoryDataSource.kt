package com.lalosapps.freetoplay.ui.main.fake

import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import kotlinx.coroutines.flow.*

object FakeGamesRepositoryDataSource {

    private val game = Game(
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

    var gameList = listOf(game)

    var gameDetails = GameDetails(
        description = "Turn-based card game with focus on control decks.",
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 1",
        gameUrl = "Game Url 1",
        genre = "Card Game",
        id = 1,
        minSystemRequirements = null,
        platform = "Windows",
        publisher = "XYZ Inc",
        releaseDate = "2022-11-17",
        screenshots = emptyList(),
        shortDescription = "Turn-based card game with focus on control decks.",
        status = "Active",
        thumbnail = "Thumbnail 1",
        title = "Titans",
        isFavorite = false
    )

    var gameDetailsList = listOf(gameDetails)

    private val allGamesStream = MutableStateFlow(gameList)
    val allGamesFlow = allGamesStream.asStateFlow()

    private val gameStream = MutableStateFlow(gameDetailsList)

    private val favoritesStream = MutableStateFlow(emptyList<GameDetails>())
    val favoritesFlow = favoritesStream.asStateFlow()

    fun toggleFavorite(id: Int, favorite: Boolean) {
        val found = gameDetailsList.find { it.id == id }
        found?.let {
            gameDetails = it.copy(isFavorite = favorite)
            gameDetailsList = listOf(gameDetails)
            gameStream.value = gameDetailsList
            favoritesStream.value = gameDetailsList.filter { g -> g.isFavorite }
        }
    }

    fun getGameFlow(id: Int): Flow<List<GameDetails>> {
        return gameStream.map { list ->
            val found = list.find { it.id == id }
            found?.let {
                listOf(it)
            } ?: emptyList()
        }
    }
}