package com.lalosapps.freetoplay.ui.screens.search

import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGamesRepository : GamesRepository {

    override suspend fun getAllGames(source: DataSource): Resource<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getGamesFlow(): Flow<List<Game>> {
        return flowOf(FakeSearchScreenDataSource.manyGames)
    }

    override suspend fun getGame(gameId: Int): Resource<GameDetails> {
        TODO("Not yet implemented")
    }

    override fun getGameFlow(id: Int): Flow<List<GameDetails>> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteGame(id: Int, favorite: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getFavoritesFlow(): Flow<List<GameDetails>> {
        TODO("Not yet implemented")
    }
}