package com.lalosapps.freetoplay.ui.screens.favorites

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lalosapps.freetoplay.R
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun favoritesScreen_onEmptyFavGamesList_verifyTopBarAndNothingYetTextShown() {
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = FakeFavoritesScreenDataSource.emptyFavorites,
                genres = FakeFavoritesScreenDataSource.emptyGenres,
                showFilter = false,
                onFilterToggle = { },
                onFilterByGenre = { },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val backButtonContentDesc = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule
            .onNodeWithContentDescription(backButtonContentDesc)
            .assertIsDisplayed()
            .assertHasClickAction()

        val topBarText = composeTestRule.activity.getString(R.string.my_games)
        composeTestRule
            .onNodeWithText(topBarText)
            .assertIsDisplayed()

        val nothingYetText = composeTestRule.activity.getString(R.string.nothing_yet)
        composeTestRule
            .onNodeWithText(nothingYetText)
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_onPopulatedFavGamesList_verifyTopBarWithBadgeAndFilterIconAndLazyColumnWithGamesShown() {
        val favorites = FakeFavoritesScreenDataSource.allFavorites
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = favorites,
                genres = FakeFavoritesScreenDataSource.allGenres,
                showFilter = false,
                onFilterToggle = { },
                onFilterByGenre = { },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val badgeText = favorites.size.toString()
        composeTestRule
            .onNodeWithText(badgeText)
            .assertIsDisplayed()

        val filterIconContentDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContentDesc)
            .assertIsDisplayed()
            .assertHasClickAction()

        val listTag = "GamesVerticalList"
        composeTestRule
            .onNodeWithTag(listTag)
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(favorites.size)
    }

    @Test
    fun favoritesScreen_onPopulatedFavGamesList_clickOnFilterIcon_verifyGenresFromFavoritesShown() {
        val favorites = FakeFavoritesScreenDataSource.allFavorites
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = favorites,
                genres = FakeFavoritesScreenDataSource.allGenres,
                showFilter = FakeFavoritesScreenDataSource.showFilter.collectAsState().value,
                onFilterToggle = { FakeFavoritesScreenDataSource.setShowFilter(it) },
                onFilterByGenre = { },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val filterIconContentDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContentDesc)
            .performClick()

        val listTag = "GenresHorizontalList"
        composeTestRule
            .onNodeWithTag(listTag)
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(favorites.size)
            .onFirst()
            .assert(hasText(favorites.first().genre))

        composeTestRule
            .onNodeWithTag(listTag)
            .onChildren()
            .filter(hasClickAction())
            .onLast()
            .assert(hasText(favorites.last().genre))
    }

    @Test
    fun favoritesScreen_onPopulatedFavGamesList_clickOnFilterIconThenOnFirstGenre_verifyOnlyTheGamesForThatGenreShown() {
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = FakeFavoritesScreenDataSource.favorites.collectAsState().value,
                genres = FakeFavoritesScreenDataSource.genres.collectAsState().value,
                showFilter = true,
                onFilterToggle = { },
                onFilterByGenre = { FakeFavoritesScreenDataSource.filterByGenreOnce(it) },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val genresTag = "GenresHorizontalList"
        composeTestRule
            .onNodeWithTag(genresTag)
            .onChildren()
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        val gamesTag = "GamesVerticalList"
        composeTestRule
            .onNodeWithTag(gamesTag)
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeFavoritesScreenDataSource.allFavorites.first().title))
    }

    @Test
    fun favoritesScreen_onPopulatedFavGamesList_clickOnFilterIconThenOnLastGenre_verifyOnlyTheGamesForThatGenreShown() {
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = FakeFavoritesScreenDataSource.favorites.collectAsState().value,
                genres = FakeFavoritesScreenDataSource.genres.collectAsState().value,
                showFilter = true,
                onFilterToggle = { },
                onFilterByGenre = { FakeFavoritesScreenDataSource.filterByGenreOnce(it) },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val genresTag = "GenresHorizontalList"
        composeTestRule
            .onNodeWithTag(genresTag)
            .onChildren()
            .filter(hasClickAction())
            .onLast()
            .performClick()

        val gamesTag = "GamesVerticalList"
        composeTestRule
            .onNodeWithTag(gamesTag)
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeFavoritesScreenDataSource.allFavorites.last().title))
    }

    @Test
    fun favoritesScreen_onPopulatedFavGamesList_clickOnFilterIconThenOnOneGenreThenClickOnFilterIconAgain_verifyGenresListHiddenAndAllFavoriteGamesShown() {
        composeTestRule.setContent {
            FavoritesScreen(
                favorites = FakeFavoritesScreenDataSource.favorites.collectAsState().value,
                genres = FakeFavoritesScreenDataSource.genres.collectAsState().value,
                showFilter = FakeFavoritesScreenDataSource.showFilter.collectAsState().value,
                onFilterToggle = { FakeFavoritesScreenDataSource.setShowFilter(it) },
                onFilterByGenre = { FakeFavoritesScreenDataSource.filterByGenreOnce(it) },
                onBackPress = { },
                onItemClick = { }
            )
        }

        val filterIconContentDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        val filterIcon = composeTestRule.onNodeWithContentDescription(filterIconContentDesc)
        filterIcon.performClick()

        val genresTag = "GenresHorizontalList"
        val genres = composeTestRule.onNodeWithTag(genresTag)
        genres.onChildren()
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        val gamesTag = "GamesVerticalList"
        val games = composeTestRule.onNodeWithTag(gamesTag)
        games.onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeFavoritesScreenDataSource.allFavorites.first().title))

        filterIcon.performClick()

        genres.assertDoesNotExist()
        games.onChildren()
            .filter(hasClickAction())
            .assertCountEquals(2)
    }
}