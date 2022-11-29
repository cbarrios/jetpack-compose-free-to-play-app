package com.lalosapps.freetoplay.ui.main.fake

import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class FakeGamesRepository : GamesRepository {

    var throwsException = false
    val throwable = Throwable("Suspend fun failed...")
    var errorResponse = false

    override suspend fun getAllGames(source: DataSource): Resource<List<Game>> {
        if (throwsException) return Resource.Error(error = throwable)

        if (source == DataSource.Remote) {
            if (errorResponse) {
                delay(5000)
                return Resource.Error(data = emptyList())
            }
            delay(3000)
            return Resource.Success(FakeGamesRepositoryDataSource.gameList)
        } else {
            return Resource.Success(FakeGamesRepositoryDataSource.gameList)
        }
    }

    override fun getGamesFlow(): Flow<List<Game>> {
        return FakeGamesRepositoryDataSource.allGamesFlow
    }

    override suspend fun getGame(gameId: Int): Resource<GameDetails> {
        if (throwsException) return Resource.Error(error = throwable)
        if (errorResponse) return Resource.Error()
        if (FakeGamesRepositoryDataSource.gameDetailsList.find { it.id == gameId } == null) return Resource.Error()
        return Resource.Success(FakeGamesRepositoryDataSource.gameDetails)
    }

    override fun getGameFlow(id: Int): Flow<List<GameDetails>> {
        return FakeGamesRepositoryDataSource.getGameFlow(id)
    }

    override suspend fun toggleFavoriteGame(id: Int, favorite: Boolean): Boolean {
        if (throwsException) return false
        FakeGamesRepositoryDataSource.toggleFavorite(id, !favorite)
        return true
    }

    override fun getFavoritesFlow(): Flow<List<GameDetails>> {
        return FakeGamesRepositoryDataSource.favoritesFlow
    }
}