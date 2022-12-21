package com.lalosapps.freetoplay.ui.screens.game_details

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.ui.main.hasIcon
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalPagerApi::class)
class GameDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun gameDetailsScreen_onUiStateLoading_verifyLoadingScreen() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateLoading,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("GameDetailsScreenLoadingProgress")
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateError_verifyErrorScreen() {
        val uiState = FakeGameDetailsScreenDataSource.uiStateError
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = uiState,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        val backButtonContentDesc = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule
            .onNodeWithContentDescription(backButtonContentDesc)
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithTag("GameDetailsNavBarTitle")
            .assertExists()
            .assertIsNotDisplayed()

        val errorImageContentDesc = composeTestRule.activity.getString(R.string.ic_connection_error)
        composeTestRule
            .onNodeWithContentDescription(errorImageContentDesc)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(uiState.error.toString())
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefault_verifySuccessScreen() {
        val uiState = FakeGameDetailsScreenDataSource.uiStateSuccess
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = uiState,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        val backButtonContentDesc = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule
            .onNodeWithContentDescription(backButtonContentDesc)
            .assertIsDisplayed()
            .assertHasClickAction()

        val topBarTitle = uiState.data.title
        composeTestRule
            .onNodeWithTag("GameDetailsNavBarTitle")
            .assertIsDisplayed()
            .assert(hasText(topBarTitle))

        composeTestRule
            .onNodeWithTag("GameDetailsContainer")
            .assertIsDisplayed()

        val favoriteIconContentDesc =
            composeTestRule.activity.getString(R.string.toggle_favorite_content_description)
        composeTestRule
            .onNodeWithContentDescription(favoriteIconContentDesc)
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefaultNonFavorite_verifyFavoriteBorderIcon() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateSuccess,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNode(hasIcon(Icons.Default.FavoriteBorder.name))
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefaultFavorite_verifyFavoriteIcon() {
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            FakeGameDetailsScreenDataSource.gameDetailsDefault.copy(
                isFavorite = true
            )
        )
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateSuccess,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNode(hasIcon(Icons.Default.Favorite.name))
            .assertIsDisplayed()
    }
}