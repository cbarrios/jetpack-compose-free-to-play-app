package com.lalosapps.freetoplay.data.repository.mockwebserver

import com.lalosapps.freetoplay.core.util.DataSource
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.data.remote.api.GamesApi
import com.lalosapps.freetoplay.data.repository.DefaultGamesRepository
import com.lalosapps.freetoplay.data.repository.fake.FakeGamesDao
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.model.GameDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultGamesRepositoryMockWebServerTest {

    // Will be testing this repo for the methods involving GamesApi ONLY.

    private lateinit var repository: DefaultGamesRepository
    private lateinit var api: GamesApi
    private lateinit var dao: FakeGamesDao
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(GamesApi::class.java)
        dao = FakeGamesDao()
        repository = DefaultGamesRepository(api, dao)
    }

    @Test
    fun getAllGames_onValidGameListResponse_verifyGamesEndpointAndResourceSuccessWithValidListSize() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(MockWebServerDataSource.validGameListResponse)
            )

            // When
            val actualResource = repository.getAllGames(DataSource.Remote)

            // Then
            val request = mockWebServer.takeRequest()
            assertEquals(
                "/games",
                request.path
            )
            assertEquals(
                true,
                actualResource is Resource.Success
            )
            assertEquals(
                MockWebServerDataSource.validGameListSize,
                (actualResource as Resource.Success).data.size
            )
        }

    @Test
    fun getAllGames_onEmptyGameListResponse_verifyResourceSuccessWithEmptyList() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(MockWebServerDataSource.emptyGameListResponse)
        )

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            Resource.Success(data = emptyList<Game>()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onErrorResponse404_verifyResourceErrorWithEmptyList() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse().setResponseCode(404)
        )

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            Resource.Error(data = emptyList<Game>()),
            actualResource
        )
    }

    @Test
    fun getAllGames_onInvalidGameListResponse_verifyResourceErrorWithThrowable() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse().setBody(MockWebServerDataSource.invalidGameListResponse)
        )

        // When
        val actualResource = repository.getAllGames(DataSource.Remote)

        // Then
        assertEquals(
            true,
            actualResource is Resource.Error
        )
        assertEquals(
            true,
            (actualResource as Resource.Error).error != null
        )
    }

    @Test
    fun getGame_onValidGameResponse_verifyGameWithQueryEndpointAndResourceSuccessWithGameFromApi() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(MockWebServerDataSource.validGameResponse)
            )

            // When
            val gameId = MockWebServerDataSource.validGameId
            val actualResource = repository.getGame(gameId)

            // Then
            val request = mockWebServer.takeRequest()
            assertEquals(
                "/game?id=$gameId",
                request.path
            )
            assertEquals(
                true,
                actualResource is Resource.Success
            )
            assertEquals(
                gameId,
                (actualResource as Resource.Success).data.id
            )
        }

    @Test
    fun getGame_onErrorResponse404_verifyResourceError() = runTest {
        // Given
        mockWebServer.enqueue(
            // This simulates a null body due to invalid endpoint(missing query) or game not found
            MockResponse().setResponseCode(404)
        )

        // When
        val actualResource = repository.getGame(MockWebServerDataSource.validGameId)

        // Then
        assertEquals(
            Resource.Error<GameDetails>(),
            actualResource
        )
    }

    @Test
    fun getGame_onInvalidGameResponse_verifyResourceErrorWithThrowable() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse().setBody(MockWebServerDataSource.invalidGameResponse)
        )

        // When
        val actualResource = repository.getGame(MockWebServerDataSource.validGameId)

        // Then
        assertEquals(
            true,
            actualResource is Resource.Error
        )
        assertEquals(
            true,
            (actualResource as Resource.Error).error != null
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}