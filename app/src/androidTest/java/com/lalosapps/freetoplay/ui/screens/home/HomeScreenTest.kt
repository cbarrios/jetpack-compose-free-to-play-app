package com.lalosapps.freetoplay.ui.screens.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.R
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalPagerApi::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeScreen_onEmptyGamesListAndUiStateSuccess_verifyTextShowsThatThereAreNoGamesAvailable() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateSuccess,
                games = FakeHomeScreenDataSource.emptyGames,
                barTitle = stringResource(id = R.string.app_name),
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        val text = composeTestRule.activity.getString(R.string.sorry_no_games_available)
        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_onEmptyGamesListAndUiStateErrorWithThrowable_verifyConnectionErrorImageAndTextWithErrorDescription() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateErrorWithThrowable,
                games = FakeHomeScreenDataSource.emptyGames,
                barTitle = stringResource(id = R.string.app_name),
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        val contentDescription = composeTestRule.activity.getString(R.string.ic_connection_error)
        composeTestRule
            .onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(FakeHomeScreenDataSource.uiStateErrorWithThrowable.error.toString())
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_onEmptyGamesListAndUiStateErrorWithoutThrowable_verifyConnectionErrorImageAndTextShowsThatGamesCouldNotBeFetched() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateErrorPlain,
                games = FakeHomeScreenDataSource.emptyGames,
                barTitle = stringResource(id = R.string.app_name),
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        val contentDescription = composeTestRule.activity.getString(R.string.ic_connection_error)
        composeTestRule
            .onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
        val text = composeTestRule.activity.getString(R.string.could_not_fetch_games)
        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_onPopulatedGamesList_regardlessOfUiState_verifyGamesScreenTopBarShown() {
        val barTitle = composeTestRule.activity.getString(R.string.app_name)
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateSuccess, // can be any
                games = FakeHomeScreenDataSource.games,
                barTitle = barTitle,
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        val menuButton = composeTestRule.activity.getString(R.string.menu_button)
        composeTestRule
            .onNodeWithContentDescription(menuButton)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("Bar Title")
            .assert(hasText(barTitle))
        val searchButton = composeTestRule.activity.getString(R.string.search_button)
        composeTestRule
            .onNodeWithContentDescription(searchButton)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_onPopulatedGamesList_regardlessOfUiState_verifyGamesScreenLazyVerticalGridShown() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateSuccess, // can be any
                games = FakeHomeScreenDataSource.games,
                barTitle = stringResource(id = R.string.app_name),
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        composeTestRule
            .onNodeWithTag("Games")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(FakeHomeScreenDataSource.games.size + 1) // games (2) plus header node (CarouselView). This might be 5 max if 4 games are shown.
            .filter(hasClickAction())
            .assertCountEquals(FakeHomeScreenDataSource.games.size) // max is 4 if 4 games are shown.
            .onFirst()
            .assert(hasText(FakeHomeScreenDataSource.games.first().title))

        composeTestRule
            .onNodeWithTag("Games")
            .onChildren()
            .filter(hasClickAction())
            .onLast()
            .assert(hasText(FakeHomeScreenDataSource.games.last().title))
    }

    @Test
    fun homeScreen_onPopulatedListWithManyGames_regardlessOfUiState_verifyGamesScreenListAndUpArrowShowsUpWhenScrollingDownAndViceVersa() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = FakeHomeScreenDataSource.uiStateSuccess, // can be any
                games = FakeHomeScreenDataSource.manyGames,
                barTitle = stringResource(id = R.string.app_name),
                onOpenDrawer = { },
                onSearch = { },
                onGameClick = { }
            )
        }

        val list = composeTestRule.onNodeWithTag("Games")

        list.assertIsDisplayed()
            .onChildren()
            .assertCountEquals(5)
            .filter(hasClickAction())
            .assertCountEquals(4)
            .onFirst()
            .assert(hasText(FakeHomeScreenDataSource.manyGames.first().title))

        list.onChildren()
            .filter(hasClickAction())
            .onLast()
            .assert(hasText(FakeHomeScreenDataSource.manyGames[3].title))

        list.performScrollToIndex(FakeHomeScreenDataSource.manyGames.lastIndex)

        list.onChildren()
            .filter(hasClickAction())
            .onLast()
            .assert(hasText(FakeHomeScreenDataSource.manyGames.last().title))

        val contentDescription =
            composeTestRule.activity.getString(R.string.scroll_up_content_description)
        composeTestRule
            .onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()

        list.performScrollToIndex(0)

        composeTestRule.onNodeWithContentDescription(contentDescription).assertDoesNotExist()
    }

}