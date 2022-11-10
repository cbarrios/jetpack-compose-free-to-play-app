package com.lalosapps.freetoplay.data.repository

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.data.local.room.dao.GamesDao
import com.lalosapps.freetoplay.data.remote.api.GamesApi
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultGamesRepository @Inject constructor(
    private val gamesApi: GamesApi,
    private val gamesDao: GamesDao
) : GamesRepository {

    override suspend fun getAllGames(): Resource<List<Game>> {
        return try {
            val response = gamesApi.getAllGames()
            response.body()?.let { dtoList ->
                gamesDao.clearAllGames()
                gamesDao.saveAllGames(dtoList.map { it.toGameEntity() })
                Resource.Success(dtoList.map { it.toGame() })
            } ?: Resource.Error(emptyList())
        } catch (t: Throwable) {
            Resource.Error(error = t)
        }
    }

    override fun getGamesFlow(): Flow<List<Game>> {
        return gamesDao.getGamesFlow().map { it.map { entity -> entity.toGame() } }
    }

    override suspend fun getGame(gameId: Int): Resource<GameDetails> {
        return try {
            val response = gamesApi.getGame(gameId)
            response.body()?.let { dtoGame ->
                val cache = gamesDao.getGameDetails(dtoGame.id)
                if (cache.isNotEmpty()) {
                    val isFavorite = cache.first().isFavorite
                    gamesDao.saveGameDetails(dtoGame.toGameDetailsEntity(isFavorite))
                } else {
                    gamesDao.saveGameDetails(dtoGame.toGameDetailsEntity(false))
                }
                Resource.Success(dtoGame.toGameDetails())
            } ?: Resource.Error()
        } catch (t: Throwable) {
            Resource.Error(error = t)
        }
    }

    override fun getGameFlow(id: Int): Flow<List<GameDetails>> {
        return gamesDao.getGameDetailsFlow(id).map { it.map { entity -> entity.toGameDetails() } }
    }

    override suspend fun toggleFavoriteGame(id: Int, favorite: Boolean): Boolean {
        return try {
            gamesDao.toggleFavoriteGameDetails(id, !favorite)
            true
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }
}