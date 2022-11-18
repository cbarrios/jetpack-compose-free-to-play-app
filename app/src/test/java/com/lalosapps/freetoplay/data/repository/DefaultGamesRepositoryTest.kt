package com.lalosapps.freetoplay.data.repository

import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import com.lalosapps.freetoplay.data.repository.fake.FakeApiDataSource
import com.lalosapps.freetoplay.data.repository.fake.FakeDaoDataSource
import com.lalosapps.freetoplay.data.repository.fake.FakeGamesApi
import com.lalosapps.freetoplay.data.repository.fake.FakeGamesDao
import com.lalosapps.freetoplay.domain.model.Game
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

    @Before
    fun tearDown() {
        Dispatchers.resetMain()
    }
}