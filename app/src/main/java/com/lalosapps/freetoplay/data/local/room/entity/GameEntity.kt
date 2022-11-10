package com.lalosapps.freetoplay.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lalosapps.freetoplay.domain.model.Game

@Entity(tableName = "game_entity")
data class GameEntity(
    @ColumnInfo(name = "developer") val developer: String,
    @ColumnInfo(name = "free_to_game_profile_url") val freeToGameProfileUrl: String,
    @ColumnInfo(name = "game_url") val gameUrl: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "platform") val platform: String,
    @ColumnInfo(name = "publisher") val publisher: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "title") val title: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: Int
) {
    fun toGame() = Game(
        developer,
        freeToGameProfileUrl,
        gameUrl,
        genre,
        id,
        platform,
        publisher,
        releaseDate,
        shortDescription,
        thumbnail,
        title
    )
}
