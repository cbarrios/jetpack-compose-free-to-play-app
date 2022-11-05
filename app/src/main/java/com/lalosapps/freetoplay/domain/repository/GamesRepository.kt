package com.lalosapps.freetoplay.domain.repository

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game

interface GamesRepository {

    suspend fun getAllGames(): Resource<List<Game>>
}