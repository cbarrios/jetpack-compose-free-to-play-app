package com.lalosapps.freetoplay.data.repository

import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import com.lalosapps.freetoplay.data.repository.fake.FakeApiDataSource
import com.lalosapps.freetoplay.data.repository.fake.FakeDaoDataSource
import com.lalosapps.freetoplay.data.repository.fake.FakeGamesApi
import com.lalosapps.freetoplay.data.repository.fake.FakeGamesDao
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultGamesRepositoryTest {

    private lateinit var repository: DefaultGamesRepository
    private lateinit var api: FakeGamesApi
    private lateinit var dao: FakeGamesDao

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        api = FakeGamesApi()
        dao = FakeGamesDao()
        repository = DefaultGamesRepository(api, dao)
    }

    @Test
    fun getAllGames_onHappyPathRemote_verifyGamesAreCachedAndResourceSuccess() = runTest {
        // Given
        api.apply {
            throwsException = false
            errorResponse = false
        }

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            FakeApiDataSource.getGameEntitiesFromGameDtoList(),
            dao.getAllGames()
        )
        assertEquals(
            Resource.Success(FakeApiDataSource.getGamesFromGameDtoList()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onEmptyListRemote_verifyCachingClearedAndResourceSuccessWithEmptyList() =
        runTest {
            // Given
            api.apply { emptyGameDtoList = true }
            FakeDaoDataSource.populateGames()

            // When
            val actualResource = repository.getAllGames(DataSource.Remote)

            // Then
            assertEquals(
                emptyList<GameEntity>(),
                dao.getAllGames()
            )
            assertEquals(
                Resource.Success(emptyList<Game>()),
                actualResource
            )
        }

    @Test
    fun getAllGames_onErrorResponseRemote_verifyResourceErrorWithEmptyList() = runTest {
        // Given
        api.apply { errorResponse = true }

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            Resource.Error(emptyList<Game>()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onExceptionRemote_verifyResourceErrorWithThrowable() = runTest {
        // Given
        api.apply { throwsException = true }

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            Resource.Error<Game>(error = api.exception),
            actualResource
        )
    }

    @Test
    fun getAllGames_onHappyPathLocalCacheFull_verifyResourceSuccessWithCachedGames() = runTest {
        // Given
        FakeDaoDataSource.populateGames()

        // When
        val actualResource = repository.getAllGames(DataSource.Local)

        // Then
        assertEquals(
            Resource.Success(FakeDaoDataSource.getAllGamesToDomainModel()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onHappyPathLocalCacheEmpty_verifyResourceSuccessWithEmptyList() = runTest {
        // Given
        FakeDaoDataSource.populateGames(true)

        // When
        val actualResource = repository.getAllGames(DataSource.Local)

        // Then
        assertEquals(
            Resource.Success(emptyList<Game>()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onExceptionLocal_verifyResourceErrorWithThrowable() = runTest {
        // Given
        dao.apply { throwsException = true }

        // When
        val actualResource = repository.getAllGames(DataSource.Local)

        // Then
        assertEquals(
            Resource.Error<Game>(error = dao.exception),
            actualResource
        )
    }

    @Test
    fun getGamesFlow_onPopulatedCache_verifyCachedGames() = runTest {
        // Given
        FakeDaoDataSource.populateGames()

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val expected = dao.getGamesFlow().map { it.map { entity -> entity.toGame() } }.first()
            val actual = repository.getGamesFlow().first()

            // Then
            assertEquals(
                true,
                expected.isNotEmpty() && actual.isNotEmpty()
            )
            assertEquals(
                expected,
                actual
            )
        }

        job.cancel()
    }

    @Test
    fun getGamesFlow_onEmptyCache_verifyEmptyList() = runTest {
        // Given
        FakeDaoDataSource.populateGames(true)

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val actual = repository.getGamesFlow().first()

            // Then
            assertEquals(
                true,
                actual.isEmpty()
            )
        }

        job.cancel()
    }

    @Test
    fun getGame_onHappyPathUpdatedGameFromApiAndGameAlreadyCached_verifyGameRetainsFavoriteAndGetsUpdatedAndResourceSuccessWithGameFromApi() =
        runTest {
            // Given
            api.apply { getUpdatedGameDetailsDto = true }
            FakeDaoDataSource.populateGameDetailsList(startFavorite = true) // test should pass for both values

            // When
            val oldCachedGame = FakeDaoDataSource.getGameDetailsList().first()
            val actualResource = repository.getGame(oldCachedGame.id)
            val updatedCachedGame = FakeDaoDataSource.getGameDetailsList().first()

            // Then
            assertEquals(
                true,
                updatedCachedGame.isFavorite == oldCachedGame.isFavorite
            )
            assertEquals(
                true,
                oldCachedGame != updatedCachedGame
            )
            assertEquals(
                Resource.Success(FakeApiDataSource.updatedGameDetailsDto.toGameDetails()),
                actualResource
            )
        }

    @Test
    fun getGame_onHappyPathSameGameFromApiAndGameAlreadyCached_verifyGameRetainsFavoriteAndGetsReplacedWithSameContentAndResourceSuccessWithGameFromApi() =
        runTest {
            // Given
            api.apply { getUpdatedGameDetailsDto = false }
            FakeDaoDataSource.populateGameDetailsList(startFavorite = true) // test should pass for both values

            // When
            val oldCachedGame = FakeDaoDataSource.getGameDetailsList().first()
            val actualResource = repository.getGame(oldCachedGame.id)
            val updatedCachedGame = FakeDaoDataSource.getGameDetailsList().first()

            // Then
            assertEquals(
                true,
                updatedCachedGame.isFavorite == oldCachedGame.isFavorite
            )
            assertEquals(
                true,
                oldCachedGame == updatedCachedGame
            )
            assertEquals(
                Resource.Success(FakeApiDataSource.gameDetailsDto.toGameDetails()),
                actualResource
            )
        }

    @Test
    fun getGame_onHappyPathNewGameFromApiAndGameNotCached_verifyGameCachedWithFavoriteToFalseAndResourceSuccessWithGameFromApi() =
        runTest {
            // Given
            FakeDaoDataSource.populateGameDetailsList(startEmpty = true)

            // When
            val actualResource = repository.getGame(FakeApiDataSource.gameDetailsDto.id)
            val cachedGame = FakeDaoDataSource.getGameDetailsList().first()

            // Then
            assertEquals(
                FakeApiDataSource.gameDetailsDto.toGameDetailsEntity(false),
                cachedGame
            )
            assertEquals(
                Resource.Success(FakeApiDataSource.gameDetailsDto.toGameDetails()),
                actualResource
            )
        }

    @Test
    fun getGame_onApiErrorResponse_verifyResourceError() = runTest {
        // Given
        api.apply { errorResponse = true }

        // When
        val actualResource = repository.getGame(FakeApiDataSource.gameDetailsDto.id)

        // Then
        assertEquals(
            Resource.Error<GameDetails>(),
            actualResource
        )
    }

    @Test
    fun getGame_onApiException_verifyResourceErrorWithThrowable() = runTest {
        // Given
        api.apply { throwsException = true }

        // When
        val actualResource = repository.getGame(FakeApiDataSource.gameDetailsDto.id)

        // Then
        assertEquals(
            Resource.Error<GameDetails>(error = api.exception),
            actualResource
        )
    }

    @Test
    fun getGame_onDatabaseException_verifyResourceErrorWithThrowable() = runTest {
        // Given
        dao.apply { throwsException = true }

        // When
        val actualResource = repository.getGame(FakeApiDataSource.gameDetailsDto.id)

        // Then
        assertEquals(
            Resource.Error<GameDetails>(error = dao.exception),
            actualResource
        )
    }

    @Test
    fun getGameFlow_onPopulatedCacheWithThisGameDetails_verifyResultListWithSingleGameDetailsAndSameAsInCache() =
        runTest {
            // Given
            FakeDaoDataSource.populateGameDetailsList()

            // When
            val job = launch(UnconfinedTestDispatcher()) {
                val expected = FakeDaoDataSource
                    .getGameDetailsFlow(FakeApiDataSource.gameDetailsDto.id)
                    .map { it.map { entity -> entity.toGameDetails() } }
                    .first()
                val actual = repository.getGameFlow(FakeApiDataSource.gameDetailsDto.id).first()

                // Then
                assertEquals(
                    1,
                    actual.size
                )
                assertEquals(
                    true,
                    expected.contains(actual.first())
                )
            }

            job.cancel()
        }

    @Test
    fun getGameFlow_onPopulatedCacheWithAnotherGameDetails_verifyResultListIsEmpty() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startDifferentId = true)

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val actual = repository.getGameFlow(FakeApiDataSource.gameDetailsDto.id).first()

            // Then
            assertEquals(
                true,
                actual.isEmpty()
            )
        }

        job.cancel()
    }

    @Test
    fun getGameFlow_onEmptyCache_verifyGameDetailsListIsEmpty() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startEmpty = true)

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val actual = repository.getGameFlow(FakeApiDataSource.gameDetailsDto.id).first()

            // Then
            assertEquals(
                true,
                actual.isEmpty()
            )
        }

        job.cancel()
    }

    @Test
    fun toggleFavoriteGame_onGameIdFoundAndNotFavorite_verifyGameSetAsFavoriteAndReturnsTrue() =
        runTest {
            // Given
            FakeDaoDataSource.populateGameDetailsList(startFavorite = false)

            // When
            val game = FakeDaoDataSource.getGameDetailsList().first()
            val actual = repository.toggleFavoriteGame(game.id, game.isFavorite)
            val updatedGame = FakeDaoDataSource.getGameDetailsList().first()

            // Then
            assertEquals(
                true,
                updatedGame.isFavorite
            )
            assertEquals(
                true,
                actual
            )
        }

    @Test
    fun toggleFavoriteGame_onGameIdFoundAndFavorite_verifyGameSetAsNotFavoriteAndReturnsTrue() =
        runTest {
            // Given
            FakeDaoDataSource.populateGameDetailsList(startFavorite = true)

            // When
            val game = FakeDaoDataSource.getGameDetailsList().first()
            val actual = repository.toggleFavoriteGame(game.id, game.isFavorite)
            val updatedGame = FakeDaoDataSource.getGameDetailsList().first()

            // Then
            assertEquals(
                false,
                updatedGame.isFavorite
            )
            assertEquals(
                true,
                actual
            )
        }

    @Test
    fun toggleFavoriteGame_onGameIdNotFound_verifyDatabaseListUnchangedAndReturnsTrue() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startEmpty = false) // should pass for both values

        // When
        val list = FakeDaoDataSource.getGameDetailsList()
        val actual = repository.toggleFavoriteGame(-1, false)
        val newList = FakeDaoDataSource.getGameDetailsList()

        // Then
        assertEquals(
            list,
            newList
        )
        assertEquals(
            true,
            actual
        )
    }

    @Test
    fun toggleFavoriteGame_onDatabaseException_verifyReturnsFalse() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startEmpty = false) // should pass for both values
        dao.apply { throwsException = true }

        // When
        val actual = repository.toggleFavoriteGame(-1, false)

        // Then
        assertEquals(
            false,
            actual
        )
    }

    @Test
    fun getFavoritesFlow_onPopulatedCacheWithSingleFavorite_verifyResultListSameAsInCache() =
        runTest {
            // Given
            FakeDaoDataSource.populateGameDetailsList(startFavorite = true)

            // When
            val job = launch(UnconfinedTestDispatcher()) {
                val actual = repository.getFavoritesFlow().first()
                val expected = FakeDaoDataSource.getGameDetailsList().map { it.toGameDetails() }

                // Then
                assertEquals(
                    1,
                    actual.size
                )
                assertEquals(
                    true,
                    expected.contains(actual.first())
                )
                assertEquals(
                    expected,
                    actual
                )
            }

            job.cancel()
        }

    @Test
    fun getFavoritesFlow_onPopulatedCacheWithSingleNotFavorite_verifyResultListIsEmpty() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startFavorite = false)

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val actual = repository.getFavoritesFlow().first()

            // Then
            assertEquals(
                true,
                actual.isEmpty()
            )
        }

        job.cancel()
    }

    @Test
    fun getFavoritesFlow_onEmptyCache_verifyResultListIsEmpty() = runTest {
        // Given
        FakeDaoDataSource.populateGameDetailsList(startEmpty = true)

        // When
        val job = launch(UnconfinedTestDispatcher()) {
            val actual = repository.getFavoritesFlow().first()

            // Then
            assertEquals(
                true,
                actual.isEmpty()
            )
        }

        job.cancel()
    }

    @Before
    fun tearDown() {
        Dispatchers.resetMain()
    }
}