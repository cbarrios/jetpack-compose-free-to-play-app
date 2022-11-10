package com.lalosapps.freetoplay.domain.repository

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    suspend fun getAllGames(): Resource<List<Game>>

    fun getGamesFlow(): Flow<List<Game>>

    suspend fun getGame(gameId: Int): Resource<GameDetails>

    fun getGameFlow(id: Int): Flow<List<GameDetails>>
}