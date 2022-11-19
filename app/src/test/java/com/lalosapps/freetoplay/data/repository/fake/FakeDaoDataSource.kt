package com.lalosapps.freetoplay.data.repository.fake

import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

object FakeDaoDataSource {

    private var games = listOf<GameEntity>()
    private val fakeGamesFlow = MutableStateFlow(games)
    private val gamesFlow = fakeGamesFlow.asStateFlow()

    private var gameDetailsList = listOf<GameDetailsEntity>()
    private val fakeGameDetailsListFlow = MutableStateFlow(gameDetailsList)
    private val gameDetailsListFlow = fakeGameDetailsListFlow.asStateFlow()

    fun populateGames(startEmpty: Boolean = false) {
        games = if (!startEmpty) {
            FakeApiDataSource.getGameEntitiesFromGameDtoList()
        } else {
            emptyList()
        }
        fakeGamesFlow.value = games
    }

    fun populateGameDetailsList(
        startEmpty: Boolean = false,
        startFavorite: Boolean = false,
        startDifferentId: Boolean = false
    ) {
        gameDetailsList = if (!startEmpty) {
            val dto = FakeApiDataSource.gameDetailsDto
            if (!startDifferentId) {
                listOf(dto.toGameDetailsEntity(startFavorite))
            } else {
                listOf(dto.copy(id = dto.id * 2).toGameDetailsEntity(startFavorite))
            }
        } else {
            emptyList()
        }
        fakeGameDetailsListFlow.value = gameDetailsList
    }

    fun saveAllGames(list: List<GameEntity>) {
        list.forEach { game ->
            val found = games.find { it.id == game.id }
            if (found == null) {
                games = games + game
            } else {
                games = games - found
                games = games + game
            }
        }
        fakeGamesFlow.value = games
    }

    fun getAllGames() = games

    fun getAllGamesToDomainModel() = games.map { it.toGame() }

    fun getAllGamesFlow() = gamesFlow

    fun getGameDetailsList() = gameDetailsList

    fun clearAllGames() {
        games = emptyList()
        fakeGamesFlow.value = games
    }

    fun saveGameDetails(gameDetails: GameDetailsEntity) {
        val found = gameDetailsList.find { it.id == gameDetails.id }
        if (found == null) {
            gameDetailsList = gameDetailsList + gameDetails
        } else {
            gameDetailsList = gameDetailsList - found
            gameDetailsList = gameDetailsList + gameDetails
        }
        fakeGameDetailsListFlow.value = gameDetailsList
    }

    fun toggleFavoriteGameDetails(id: Int, favorite: Boolean) {
        gameDetailsList =
            gameDetailsList.map { if (it.id == id) it.copy(isFavorite = favorite) else it }
        fakeGameDetailsListFlow.value = gameDetailsList
    }

    fun getGameDetails(id: Int): List<GameDetailsEntity> {
        val found = gameDetailsList.find { it.id == id }
        return if (found == null) {
            emptyList()
        } else {
            listOf(found)
        }
    }

    fun getGameDetailsFlow(id: Int): Flow<List<GameDetailsEntity>> {
        return gameDetailsListFlow
            .map { it.find { game -> game.id == id } }
            .map { if (it != null) listOf(it) else emptyList() }
    }

    fun getFavoritesFlow(): Flow<List<GameDetailsEntity>> {
        return gameDetailsListFlow.map { it.filter { game -> game.isFavorite } }
    }
}