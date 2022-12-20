package com.lalosapps.freetoplay.ui.screens.favorites

import com.lalosapps.freetoplay.core.util.getAllGenres
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.screens.base.ChipData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object FakeFavoritesScreenDataSource {

    val emptyFavorites = emptyList<Game>()
    val allFavorites = listOf(
        Game(
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
            title = "Titans",
            isFavorite = true
        ),
        Game(
            developer = "XYZ Devs",
            freeToGameProfileUrl = "Profile Url 2",
            gameUrl = "Game Url 2",
            genre = "Strategy",
            id = 2,
            platform = "Windows",
            publisher = "XYZ Inc",
            releaseDate = "2022-12-20",
            shortDescription = "Best strategy game.",
            thumbnail = "Thumbnail 2",
            title = "God of War",
            isFavorite = true
        )
    )
    val favorites = MutableStateFlow(allFavorites)

    val emptyGenres = emptyList<ChipData>()
    val allGenres = allFavorites.getAllGenres().map { ChipData(it, false) }
    val genres = MutableStateFlow(allGenres)
    fun filterByGenreOnce(genre: String) {
        genres.update { allGenres.map { if (it.text == genre) it.copy(checked = !it.checked) else it } }
        favorites.update { allFavorites.filter { it.genre == genre } }
    }

    val showFilter = MutableStateFlow(false)
    fun setShowFilter(value: Boolean) {
        showFilter.value = value
        genres.update { allGenres }
        favorites.update { allFavorites }
    }
}