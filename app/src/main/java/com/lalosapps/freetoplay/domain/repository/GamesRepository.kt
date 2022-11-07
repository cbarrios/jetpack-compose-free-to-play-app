package com.lalosapps.freetoplay.domain.repository

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails

interface GamesRepository {

    suspend fun getAllGames(): Resource<List<Game>>

    suspend fun getGame(gameId: Int): Resource<GameDetails>
}