package com.lalosapps.freetoplay.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllGames(games: List<GameEntity>)

    @Query("SELECT * FROM game_entity")
    fun getGamesFlow(): Flow<List<GameEntity>>

    @Query("DELETE from game_entity")
    suspend fun clearAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGameDetails(gameDetailsEntity: GameDetailsEntity)

    @Query("SELECT * FROM game_details_entity WHERE id = :id")
    fun getGameDetailsFlow(id: Int): Flow<List<GameDetailsEntity>>
}