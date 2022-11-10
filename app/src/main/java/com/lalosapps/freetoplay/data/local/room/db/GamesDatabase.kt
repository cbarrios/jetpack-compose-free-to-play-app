package com.lalosapps.freetoplay.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lalosapps.freetoplay.data.local.room.dao.GamesDao
import com.lalosapps.freetoplay.data.local.room.entity.Converters
import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity

@Database(entities = [GameEntity::class, GameDetailsEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class GamesDatabase : RoomDatabase() {

    abstract val gamesDao: GamesDao
}