package com.lalosapps.freetoplay.data.local.room.dao

import com.lalosapps.freetoplay.data.local.room.entity.GameDetailsEntity
import com.lalosapps.freetoplay.data.local.room.entity.GameEntity
import com.lalosapps.freetoplay.data.remote.dto.GameDetailsDto

object GamesDaoDataSource {

    val gameEntityList = listOf(
        GameEntity(
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
    )

    fun getGameDetailsEntity(isFavorite: Boolean): GameDetailsEntity {
        return GameDetailsDto(
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
        ).toGameDetailsEntity(isFavorite)
    }
}