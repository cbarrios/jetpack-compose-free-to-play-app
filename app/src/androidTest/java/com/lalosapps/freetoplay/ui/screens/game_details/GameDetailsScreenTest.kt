package com.lalosapps.freetoplay.ui.screens.game_details

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.domain.model.MinimumSystemRequirements
import com.lalosapps.freetoplay.domain.model.Screenshot
import com.lalosapps.freetoplay.ui.main.hasIcon
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalPagerApi::class)
class GameDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            FakeGameDetailsScreenDataSource.gameDetailsDefault
        )
    }

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
    fun gameDetailsScreen_onUiStateSuccessGameFavorite_verifyFavoriteIcon() {
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

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefaultEmptyScreenshots_verifySingleGameImageShown() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateSuccess,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("GameDetailsImage")
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessGameWithManyScreenshots_verifyCarouselViewShown() {
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            FakeGameDetailsScreenDataSource.gameDetailsDefault.copy(
                screenshots = listOf(
                    Screenshot(1, "1"),
                    Screenshot(2, "2"),
                    Screenshot(3, "3"),
                )
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
            .onNodeWithTag("GameDetailsCarousel")
            .assertIsDisplayed()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefaultShortDescription_verifyGameDescriptionNotExpandable() {
        val uiState = FakeGameDetailsScreenDataSource.uiStateSuccess
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = uiState,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        val expandableText = composeTestRule.onNodeWithTag("ExpandableText")
        expandableText
            .assertIsDisplayed()
            .assertTextEquals(uiState.data.description)

        expandableText
            .assert(
                !hasText(
                    composeTestRule.activity.getString(R.string.show_more),
                    substring = true
                )
            )
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessGameWithHugeDescription_verifyGameDescriptionExpandable() {
        val game = FakeGameDetailsScreenDataSource.gameDetailsDefault
        val hugeDescription =
            game.description + game.description + game.description + game.description + game.description
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            game.copy(description = hugeDescription)
        )
        val uiState = FakeGameDetailsScreenDataSource.uiStateSuccess
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = uiState,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        val expandableText = composeTestRule.onNodeWithTag("ExpandableText")
        expandableText
            .assertIsDisplayed()
            .assert(
                hasText(
                    composeTestRule.activity.getString(R.string.show_more),
                    substring = true
                )
            )

        expandableText.performClick()
        expandableText
            .assertIsDisplayed()
            .assert(
                hasText(
                    composeTestRule.activity.getString(R.string.show_less),
                    substring = true
                )
            )

        expandableText.performClick()
        expandableText
            .assertIsDisplayed()
            .assert(
                hasText(
                    composeTestRule.activity.getString(R.string.show_more),
                    substring = true
                )
            )
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefaultNullMinSystemRequirements_verifyMinimumSystemRequirementsSectionNotShown() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateSuccess,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.min_sys_reqs))
            .assertDoesNotExist()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessGameMinSysRequirementsHasAllPropertiesNull_verifyMinimumSystemRequirementsSectionNotShown() {
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            FakeGameDetailsScreenDataSource.gameDetailsDefault.copy(
                minSystemRequirements = MinimumSystemRequirements(null, null, null, null, null)
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
            .onNodeWithText(composeTestRule.activity.getString(R.string.min_sys_reqs))
            .assertDoesNotExist()
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessGameWithMinSystemRequirementsHasAtLeastOneNonNullProperty_verifyMinimumSystemRequirementsSectionShownWithTheNonNullPropertiesAndForTheRestASingleQuestionMark() {
        FakeGameDetailsScreenDataSource.setUiStateSuccessData(
            FakeGameDetailsScreenDataSource.gameDetailsDefault.copy(
                minSystemRequirements = MinimumSystemRequirements(
                    null,
                    null,
                    null,
                    "AMD Ryzen 7700X",
                    null
                )
            )
        )
        val uiState = FakeGameDetailsScreenDataSource.uiStateSuccess
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = uiState,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.min_sys_reqs))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("MinSysReqProcessor")
            .assertIsDisplayed()
            .assertTextEquals(uiState.data.minSystemRequirements?.processor!!)

        composeTestRule
            .onNodeWithTag("MinSysReqMemory")
            .assertExists()
            .assertTextEquals("?")

        composeTestRule
            .onNodeWithTag("MinSysReqStorage")
            .assertExists()
            .assertTextEquals("?")

        composeTestRule
            .onNodeWithTag("MinSysReqGraphics")
            .assertExists()
            .assertTextEquals("?")

        composeTestRule
            .onNodeWithTag("MinSysReqOS")
            .assertExists()
            .assertTextEquals("?")
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefault_verifyWebsiteLinkClickable() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateSuccess,
                onToggleFavorite = { _, _ -> },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("WebsiteLink")
            .assertIsDisplayed()
            .assert(hasClickAction())
    }

    @Test
    fun gameDetailsScreen_onUiStateSuccessDefault_clickOnFavoriteBorderIconThenClickAgain_verifySwitchToFavoriteIconThenBackToBorder() {
        composeTestRule.setContent {
            GameDetailsScreen(
                uiState = FakeGameDetailsScreenDataSource.uiStateFlowSuccess.collectAsState().value,
                onToggleFavorite = { _, _ -> FakeGameDetailsScreenDataSource.toggleFavorite() },
                onBackPress = { },
                onGameUrlClick = { }
            )
        }

        composeTestRule
            .onNode(hasIcon(Icons.Default.FavoriteBorder.name))
            .assertIsDisplayed()

        val favoriteIconContentDesc =
            composeTestRule.activity.getString(R.string.toggle_favorite_content_description)
        val toggleIcon = composeTestRule
            .onNodeWithContentDescription(favoriteIconContentDesc)

        toggleIcon.performClick()

        composeTestRule
            .onNode(hasIcon(Icons.Default.Favorite.name))
            .assertIsDisplayed()


        toggleIcon.performClick()

        composeTestRule
            .onNode(hasIcon(Icons.Default.FavoriteBorder.name))
            .assertIsDisplayed()
    }
}