package com.lalosapps.freetoplay.data.repository.fake

import com.lalosapps.freetoplay.data.local.room.dao.GamesDao
import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow

class FakeGamesDao : GamesDao {

    var throwsException = false
    var exception: Throwable? = null
    private val readException = Throwable("Read db operation went wrong.")
    private val writeException = Throwable("Write db operation went wrong.")
    private val deleteException = Throwable("Delete db operation went wrong.")

    override suspend fun saveAllGames(games: List<GameEntity>) {
        if (throwsException) {
            exception = writeException
            throw writeException
        }
        FakeDaoDataSource.saveAllGames(games)
    }

    override suspend fun getAllGames(): List<GameEntity> {
        if (throwsException) {
            exception = readException
            throw readException
        }
        return FakeDaoDataSource.getAllGames()
    }

    override fun getGamesFlow(): Flow<List<GameEntity>> {
        return FakeDaoDataSource.getAllGamesFlow()
    }

    override suspend fun clearAllGames() {
        if (throwsException) {
            exception = deleteException
            throw deleteException
        }
        FakeDaoDataSource.clearAllGames()
    }

    override suspend fun saveGameDetails(gameDetailsEntity: GameDetailsEntity) {
        if (throwsException) {
            exception = writeException
            throw writeException
        }
        FakeDaoDataSource.saveGameDetails(gameDetailsEntity)
    }

    override fun getGameDetailsFlow(id: Int): Flow<List<GameDetailsEntity>> {
        return FakeDaoDataSource.getGameDetailsFlow(id)
    }

    override suspend fun toggleFavoriteGameDetails(id: Int, favorite: Boolean) {
        if (throwsException) {
            exception = writeException
            throw writeException
        }
        FakeDaoDataSource.toggleFavoriteGameDetails(id, favorite)
    }

    override suspend fun getGameDetails(id: Int): List<GameDetailsEntity> {
        if (throwsException) {
            exception = readException
            throw readException
        }
        return FakeDaoDataSource.getGameDetails(id)
    }

    override fun getFavoritesFlow(): Flow<List<GameDetailsEntity>> {
        return FakeDaoDataSource.getFavoritesFlow()
    }
}