package com.lalosapps.freetoplay.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.model.MinimumSystemRequirements
import com.lalosapps.freetoplay.domain.model.Screenshot

@Entity(tableName = "game_details_entity")
data class GameDetailsEntity(
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "developer") val developer: String,
    @ColumnInfo(name = "free_to_game_profile_url") val freeToGameProfileUrl: String,
    @ColumnInfo(name = "game_url") val gameUrl: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "requirement_os") val requirementOs: String?,
    @ColumnInfo(name = "requirement_processor") val requirementProcessor: String?,
    @ColumnInfo(name = "requirement_memory") val requirementMemory: String?,
    @ColumnInfo(name = "requirement_storage") val requirementStorage: String?,
    @ColumnInfo(name = "requirement_graphics") val requirementGraphics: String?,
    @ColumnInfo(name = "platform") val platform: String,
    @ColumnInfo(name = "publisher") val publisher: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "screenshots") val screenshots: List<Screenshot>,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "favorite") val isFavorite: Boolean,
    @PrimaryKey @ColumnInfo(name = "id") val id: Int
) {
    private val hasMinSysRequirements: Boolean
        get() = requirementOs != null || requirementProcessor != null || requirementMemory != null || requirementStorage != null || requirementGraphics != null

    fun toGameDetails() = GameDetails(
        description,
        developer,
        freeToGameProfileUrl,
        gameUrl,
        genre,
        id,
        minSystemRequirements = if (hasMinSysRequirements)
            MinimumSystemRequirements(
                requirementGraphics,
                requirementMemory,
                requirementOs,
                requirementProcessor,
                requirementStorage
            ) else null,
        platform,
        publisher,
        releaseDate,
        screenshots,
        shortDescription,
        status,
        thumbnail,
        title,
        isFavorite
    )
}
