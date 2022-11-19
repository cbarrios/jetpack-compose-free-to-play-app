package com.lalosapps.freetoplay.data.repository.fake

import com.lalosapps.freetoplay.data.remote.dto.GameDetailsDto
import com.lalosapps.freetoplay.data.remote.dto.GameDto
import com.lalosapps.freetoplay.domain.model.MinimumSystemRequirements
import com.lalosapps.freetoplay.domain.model.Screenshot

object FakeApiDataSource {

    private val gameDto = GameDto(
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 1",
        gameUrl = "Game Url 1",
        genre = "Card Game",
        id = 1,
        platform = "Windows",
        publisher = "XYZ Inc",
        releaseDate = "2022-11-17",
        shortDescription = "Turn-based card game with focus on control decks.",
        thumbnail = "Thumbnail 1",
        title = "Titans"
    )

    val gameDtoList = listOf(gameDto)

    fun getGameEntitiesFromGameDtoList() = gameDtoList.map { it.toGameEntity() }

    fun getGamesFromGameDtoList() = gameDtoList.map { it.toGame() }

    val gameDetailsDto = GameDetailsDto(
        description = "Turn-based card game with focus on control decks.",
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 1",
        gameUrl = "Game Url 1",
        genre = "Card Game",
        id = 1,
        minSystemRequirements = null,
        platform = "Windows",
        publisher = "XYZ Inc",
        releaseDate = "2022-11-17",
        screenshots = emptyList(),
        shortDescription = "Turn-based card game with focus on control decks.",
        status = "Active",
        thumbnail = "Thumbnail 1",
        title = "Titans"
    )

    /**
     * The following changes were made with respect to [gameDetailsDto]:
     *
     * minSystemRequirements: null -> MinimumSystemRequirements("", "", "", "", "")
     *
     * title: "Titans" -> "New Titans"
     *
     * screenshots: emptyList() -> listOf(Screenshot(1, "image 1"))
     *
     */
    val updatedGameDetailsDto = GameDetailsDto(
        description = "Turn-based card game with focus on control decks.",
        developer = "XYZ Devs",
        freeToGameProfileUrl = "Profile Url 1",
        gameUrl = "Game Url 1",
        genre = "Card Game",
        id = 1,
        minSystemRequirements = MinimumSystemRequirements("", "", "", "", ""),
        platform = "Windows",
        publisher = "XYZ Inc",
        releaseDate = "2022-11-17",
        screenshots = listOf(Screenshot(1, "image 1")),
        shortDescription = "Turn-based card game with focus on control decks.",
        status = "Active",
        thumbnail = "Thumbnail 1",
        title = "New Titans"
    )
}