package com.lalosapps.freetoplay.ui.main

import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.usecases.GetFavoritesFlowUseCase
import com.lalosapps.freetoplay.domain.usecases.GetGamesFlowUseCase
import com.lalosapps.freetoplay.ui.main.fake.FakeGamesRepository
import com.lalosapps.freetoplay.ui.main.fake.FakeGamesRepositoryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakeGamesRepository
    private lateinit var getGamesFlowUseCase: GetGamesFlowUseCase
    private lateinit var getFavoritesFlowUseCase: GetFavoritesFlowUseCase

    @Before
    fun onBefore() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun init_onPopulatedCacheAndNoErrors_verifyUiStateSuccessAndGamesListSetCorrectlyAndSplashInvisible() =
        runTest {
            // Given
            repository = FakeGamesRepository()
            getGamesFlowUseCase = GetGamesFlowUseCase(repository)
            getFavoritesFlowUseCase = GetFavoritesFlowUseCase(repository)
            viewModel = MainViewModel(repository, getGamesFlowUseCase, getFavoritesFlowUseCase)

            // When (init block executes)
            val actualUiState = viewModel.uiState.value
            val actualGamesList = viewModel.gamesList.value
            val actualSplashScreenVisible = viewModel.splashScreenVisible

            // Then
            assertEquals(
                Resource.Success(FakeGamesRepositoryDataSource.gameList),
                actualUiState
            )
            assertEquals(
                FakeGamesRepositoryDataSource.gameList,
                actualGamesList
            )
            assertEquals(
                false,
                actualSplashScreenVisible
            )
        }

    @Test
    fun init_onPopulatedCacheWithRemoteError_verifyUiStateSuccessThenErrorWithEmptyListAndGamesListSetCorrectlyAndSplashInvisible() =
        runTest {
            // Given
            repository = FakeGamesRepository().apply { errorResponse = true }
            getGamesFlowUseCase = GetGamesFlowUseCase(repository)
            getFavoritesFlowUseCase = GetFavoritesFlowUseCase(repository)
            viewModel = MainViewModel(repository, getGamesFlowUseCase, getFavoritesFlowUseCase)

            // When (init block executes)
            val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
            val actualGamesList = viewModel.gamesList.value
            val actualSplashScreenVisible = viewModel.splashScreenVisible

            // Then
            assertEquals(
                true,
                viewModel.uiState.value is Resource.Success
            )
            // simulated delay when error from DataSource.Remote, so we need this. Please see getAllGames function in FakeGamesRepository.
            advanceUntilIdle()
            assertEquals(
                Resource.Error<List<Game>>(data = emptyList()),
                viewModel.uiState.value
            )
            assertEquals(
                FakeGamesRepositoryDataSource.gameList,
                actualGamesList
            )
            assertEquals(
                false,
                actualSplashScreenVisible
            )

            collectJob.cancel()
        }

    @Test
    fun init_onLocalException_verifyUiStateErrorWithThrowableAndGamesListEmptyAndSplashInvisible() =
        runTest {
            // Given
            repository = FakeGamesRepository().apply { throwsException = true }
            getGamesFlowUseCase = GetGamesFlowUseCase(repository)
            getFavoritesFlowUseCase = GetFavoritesFlowUseCase(repository)
            viewModel = MainViewModel(repository, getGamesFlowUseCase, getFavoritesFlowUseCase)

            // When (init block executes)
            val actualUiState = viewModel.uiState.value
            val actualGamesList = viewModel.gamesList.value
            val actualSplashScreenVisible = viewModel.splashScreenVisible

            // Then
            assertEquals(
                Resource.Error<List<Game>>(error = repository.throwable),
                actualUiState
            )
            assertEquals(
                emptyList<Game>(),
                actualGamesList
            )
            assertEquals(
                false,
                actualSplashScreenVisible
            )
        }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }
}