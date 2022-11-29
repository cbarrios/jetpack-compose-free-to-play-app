package com.lalosapps.freetoplay.data.local.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lalosapps.freetoplay.data.local.room.db.GamesDatabase
import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class GamesDaoTest {

    private lateinit var db: GamesDatabase
    private lateinit var dao: GamesDao

    @Before
    fun onBefore() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GamesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.gamesDao
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun saveAllGames_and_getAllGames_verifyInsertedList() = runTest {
        // Given
        val expected = GamesDaoDataSource.gameEntityList

        // When
        dao.saveAllGames(expected)
        val actual = dao.getAllGames()

        // Then
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun saveAllGames_and_getGamesFlow_verifyInsertedListFromFlow() = runTest {
        // Given
        val expected = GamesDaoDataSource.gameEntityList

        // When
        dao.saveAllGames(expected)
        val actual = dao.getGamesFlow().first()

        // Then
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun saveAllGames_and_getGamesFlow_and_clearAllGames_verifyFlowEmitsSavedListThenEmptyList() =
        runTest {
            // Given
            val expected = GamesDaoDataSource.gameEntityList

            // When (1)
            dao.saveAllGames(expected)
            val actualSaved = dao.getGamesFlow().first()

            // Then (1)
            assertEquals(
                expected,
                actualSaved
            )

            // When (2)
            dao.clearAllGames()
            val actualEmpty = dao.getGamesFlow().first()

            // Then (2)
            assertEquals(
                emptyList<GameEntity>(),
                actualEmpty
            )
        }

    @Test
    fun getAllGames_onEmptyDatabase_verifyEmptyList() = runTest {
        // When
        val actual = dao.getAllGames()

        // Then
        assertEquals(
            true,
            actual.isEmpty()
        )
    }

    @Test
    fun getGamesFlow_onEmptyDatabase_verifyEmptyList() = runTest {
        // When
        val actual = dao.getGamesFlow().first()

        // Then
        assertEquals(
            true,
            actual.isEmpty()
        )
    }

    @Test
    fun clearAllGames_onEmptyDatabase_verifyEmptyList() = runTest {
        // When
        dao.clearAllGames()
        val actual = dao.getGamesFlow().first()

        // Then
        assertEquals(
            true,
            actual.isEmpty()
        )
    }

    @Test
    fun getGamesFlow_onTwoInsertionsAndOneDeletion_verifyThreeCollectionsAndResultListNotEmpty() =
        runTest {
            // Given
            var actualGames = listOf<GameEntity>()
            var collections = 0
            dao.saveAllGames(GamesDaoDataSource.gameEntityList)

            // When
            val job = launch(UnconfinedTestDispatcher()) {
                dao.getGamesFlow().collect {
                    collections++
                    actualGames = it
                }
                dao.clearAllGames()
                dao.saveAllGames(GamesDaoDataSource.gameEntityList)

                // Then
                assertEquals(
                    3,
                    collections
                )
                assertEquals(
                    true,
                    actualGames.isNotEmpty()
                )
            }

            job.cancel()
        }

    @Test
    fun saveGameDetails_and_getGameDetails_onEmptyCache_verifyInsertion() = runTest {
        // Given
        val game = GamesDaoDataSource.getGameDetailsEntity(false)
        val expected = listOf(game)

        // When
        dao.saveGameDetails(game)
        val actual = dao.getGameDetails(game.id)

        // Then
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun saveGameDetails_and_getGameDetails_onPopulatedCache_verifyInsertedGameWithSameIdGetsReplaced() =
        runTest {
            // Given
            val game = GamesDaoDataSource.getGameDetailsEntity(false)

            // When (1)
            dao.saveGameDetails(game)

            // Then (1)
            assertEquals(
                false,
                dao.getGameDetails(game.id).first().isFavorite
            )

            // When (2)
            val updatedGame = game.copy(isFavorite = true)
            dao.saveGameDetails(updatedGame)
            val actualGame = dao.getGameDetails(game.id).first()

            // Then (2)
            assertEquals(
                true,
                actualGame.isFavorite
            )
            assertEquals(
                game.copy(isFavorite = true),
                actualGame
            )
        }

    @Test
    fun saveGameDetails_and_getGameDetailsFlow_onEmptyCache_verifyFlowEmitsNewInsertedGame() =
        runTest {
            // Given
            val game = GamesDaoDataSource.getGameDetailsEntity(false)
            val expected = listOf(game)

            // When
            dao.saveGameDetails(game)
            val actual = dao.getGameDetailsFlow(game.id).first()

            // Then
            assertEquals(
                expected,
                actual
            )
        }

    @Test
    fun saveGameDetails_and_toggleFavoriteGameDetails_and_getGameDetailsFlow_onEmptyCache_verifyFlowEmitsNewInsertedGameWithFavoriteToggled() =
        runTest {
            // Given
            val game = GamesDaoDataSource.getGameDetailsEntity(false)
            val expected = game.copy(isFavorite = true)

            // When
            dao.saveGameDetails(game)
            dao.toggleFavoriteGameDetails(game.id, !game.isFavorite)
            val actual = dao.getGameDetailsFlow(game.id).first()

            // Then
            assertEquals(
                expected,
                actual.first()
            )
        }

    @Test
    fun getFavoritesFlow_onEmptyCacheInsertOneGameToggleFavoriteThenToggleAgain_verifyFavoritesListIncludesGameThenBecomesEmpty() =
        runTest {
            // Given
            val game = GamesDaoDataSource.getGameDetailsEntity(false)
            var actualFavorites = listOf<GameDetailsEntity>()

            val job = launch(UnconfinedTestDispatcher()) {
                // When (1)
                dao.saveGameDetails(game)
                dao.getFavoritesFlow().collect {
                    actualFavorites = it
                }
                dao.toggleFavoriteGameDetails(
                    actualFavorites.first().id,
                    !actualFavorites.first().isFavorite
                )

                // Then (1)
                assertEquals(
                    game.copy(isFavorite = true),
                    actualFavorites.first()
                )

                // When (2)
                dao.toggleFavoriteGameDetails(
                    actualFavorites.first().id,
                    !actualFavorites.first().isFavorite
                )

                // Then (2)
                assertEquals(
                    emptyList<GameDetailsEntity>(),
                    actualFavorites
                )
            }

            job.cancel()
        }
}