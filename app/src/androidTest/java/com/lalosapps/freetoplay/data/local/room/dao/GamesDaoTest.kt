package com.lalosapps.freetoplay.data.local.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lalosapps.freetoplay.data.local.room.db.GamesDatabase
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
}