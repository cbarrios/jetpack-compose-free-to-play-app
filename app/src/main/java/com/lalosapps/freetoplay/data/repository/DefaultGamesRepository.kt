package com.lalosapps.freetoplay.data.repository

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.data.remote.api.GamesApi
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import javax.inject.Inject

class DefaultGamesRepository @Inject constructor(
    private val gamesApi: GamesApi
) : GamesRepository {

    override suspend fun getAllGames(): Resource<List<Game>> {
        return try {
            val response = gamesApi.getAllGames()
            response.body()?.let { dtoList ->
                Resource.Success(dtoList.map { it.toGame() })
            } ?: Resource.Error(emptyList())
        } catch (t: Throwable) {
            Resource.Error(error = t)
        }
    }
}