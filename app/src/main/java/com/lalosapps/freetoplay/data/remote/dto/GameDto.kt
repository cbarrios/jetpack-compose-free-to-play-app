package com.lalosapps.freetoplay.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import com.lalosapps.freetoplay.domain.model.Game

data class GameDto(
    @SerializedName("developer") val developer: String,
    @SerializedName("freetogame_profile_url") val freeToGameProfileUrl: String,
    @SerializedName("game_url") val gameUrl: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("id") val id: Int,
    @SerializedName("platform") val platform: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String
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

    fun toGameEntity() = GameEntity(
        developer,
        freeToGameProfileUrl,
        gameUrl,
        genre,
        platform,
        publisher,
        releaseDate,
        shortDescription,
        thumbnail,
        title,
        id
    )
}