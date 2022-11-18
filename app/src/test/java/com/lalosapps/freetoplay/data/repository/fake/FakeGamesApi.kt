package com.lalosapps.freetoplay.data.repository.fake

import com.lalosapps.freetoplay.data.remote.api.GamesApi
import com.lalosapps.freetoplay.data.remote.dto.GameDetailsDto
import com.lalosapps.freetoplay.data.remote.dto.GameDto
import okhttp3.internal.EMPTY_RESPONSE
import retrofit2.Response

class FakeGamesApi : GamesApi {

    var throwsException = false
    var exception: Throwable? = null
    private val allGamesException =
        Throwable("Something went wrong when getting the list of games.")
    private val singleGameDetailsException =
        Throwable("Something went wrong when getting this game details.")
    var errorResponse = false
    var emptyGameDtoList = false

    override suspend fun getAllGames(): Response<List<GameDto>> {
        if (throwsException) {
            exception = allGamesException
            throw allGamesException
        }
        if (errorResponse) {
            return Response.error(500, EMPTY_RESPONSE)
        }
        return Response.success(
            200,
            if (!emptyGameDtoList) FakeApiDataSource.gameDtoList else emptyList()
        )
    }

    override suspend fun getGame(gameId: Int): Response<GameDetailsDto> {
        if (throwsException) {
            exception = singleGameDetailsException
            throw singleGameDetailsException
        }
        if (errorResponse) {
            return Response.error(500, EMPTY_RESPONSE)
        }
        return Response.success(200, FakeApiDataSource.gameDetailsDto)
    }
}