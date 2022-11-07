package com.lalosapps.freetoplay.data.remote.api

import com.lalosapps.freetoplay.data.remote.dto.GameDetailsDto
import com.lalosapps.freetoplay.data.remote.dto.GameDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GamesApi {

    companion object {
        const val BASE_URL = "https://www.freetogame.com/api/"
    }

    @GET("games")
    suspend fun getAllGames(): Response<List<GameDto>>

    @GET("game")
    suspend fun getGame(
        @Query("id") gameId: Int
    ): Response<GameDetailsDto>
}