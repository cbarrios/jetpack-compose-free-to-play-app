package com.lalosapps.freetoplay.ui.main.fake

import com.lalosapps.freetoplay.core.di.AppModule
import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Module
object FakeAppModule {

    @Singleton
    @Provides
    fun provideFakeGamesRepository() = object : GamesRepository {
        override suspend fun getAllGames(source: DataSource): Resource<List<Game>> {
            return Resource.Success(FakeFreeToPlayAppDataSource.games)
        }

        override fun getGamesFlow(): Flow<List<Game>> {
            return flowOf(FakeFreeToPlayAppDataSource.games)
        }

        override suspend fun getGame(gameId: Int): Resource<GameDetails> {
            return Resource.Success(FakeFreeToPlayAppDataSource.gameDetails)
        }

        override fun getGameFlow(id: Int): Flow<List<GameDetails>> {
            return flowOf(listOf(FakeFreeToPlayAppDataSource.gameDetails))
        }

        override suspend fun toggleFavoriteGame(id: Int, favorite: Boolean): Boolean {
            return true
        }

        override fun getFavoritesFlow(): Flow<List<GameDetails>> {
            return flowOf(listOf(FakeFreeToPlayAppDataSource.gameDetails))
        }
    }
}