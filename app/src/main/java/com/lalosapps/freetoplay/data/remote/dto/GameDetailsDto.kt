package com.lalosapps.freetoplay.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.model.MinimumSystemRequirements
import com.lalosapps.freetoplay.domain.model.Screenshot

data class GameDetailsDto(
    @SerializedName("description") val description: String,
    @SerializedName("developer") val developer: String,
    @SerializedName("freetogame_profile_url") val freeToGameProfileUrl: String,
    @SerializedName("game_url") val gameUrl: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("id") val id: Int,
    @SerializedName("minimum_system_requirements") val minSystemRequirements: MinimumSystemRequirements?,
    @SerializedName("platform") val platform: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("screenshots") val screenshots: List<Screenshot>,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("status") val status: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String
) {

    fun toGameDetails() = GameDetails(
        description,
        developer,
        freeToGameProfileUrl,
        gameUrl,
        genre,
        id,
        minSystemRequirements,
        platform,
        publisher,
        releaseDate,
        screenshots,
        shortDescription,
        status,
        thumbnail,
        title
    )
}