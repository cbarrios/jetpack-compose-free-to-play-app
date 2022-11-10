package com.lalosapps.freetoplay.domain.model

data class GameDetails(
    val description: String,
    val developer: String,
    val freeToGameProfileUrl: String,
    val gameUrl: String,
    val genre: String,
    val id: Int,
    val minSystemRequirements: MinimumSystemRequirements?,
    val platform: String,
    val publisher: String,
    val releaseDate: String,
    val screenshots: List<Screenshot>,
    val shortDescription: String,
    val status: String,
    val thumbnail: String,
    val title: String,
    val isFavorite: Boolean
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
        title,
        isFavorite
    )
}
