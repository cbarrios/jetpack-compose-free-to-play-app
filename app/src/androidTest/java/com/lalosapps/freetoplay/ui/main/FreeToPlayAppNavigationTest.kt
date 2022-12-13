package com.lalosapps.freetoplay.ui.main

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource
import com.lalosapps.freetoplay.ui.screens.base.Screen
import com.lalosapps.freetoplay.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalPagerApi
@ExperimentalLifecycleComposeApi
class FreeToPlayAppNavigationTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupFreeToPlayNavHost() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            FreeToPlayApp(
                uiState = composeTestRule.activity.viewModels<MainViewModel>().value.uiState.collectAsState().value,
                gamesList = composeTestRule.activity.viewModels<MainViewModel>().value.gamesList.collectAsState().value,
                onDrawerAllGamesClick = { },
                onDrawerPcGamesClick = { },
                onDrawerWebGamesClick = { },
                onDrawerLatestGamesClick = { },
                navController = navController
            )
        }
    }

    // Verify the start destination
    @Test
    fun freeToPlayNavHost_verifyStartDestination() {
        composeTestRule.onNode(hasTestTag("Games")).assertExists()
        navController.assertCurrentRouteName(Screen.HOME)
    }

    @Test
    fun freeToPlayNavHost_verifyMenuButtonShownOnStartDestination() {
        val menuButton = composeTestRule.activity.getString(R.string.menu_button)
        composeTestRule
            .onNodeWithContentDescription(menuButton)
            .assertIsDisplayed()
    }

    @Test
    fun freeToPlayNavHost_verifySearchButtonShownOnStartDestination() {
        val searchButton = composeTestRule.activity.getString(R.string.search_button)
        composeTestRule
            .onNodeWithContentDescription(searchButton)
            .assertIsDisplayed()
    }

    @Test
    fun freeToPlayNavHost_clickMenuButton_navigatesToFavoritesScreen() {
        val menuButton = composeTestRule.activity.getString(R.string.menu_button)
        composeTestRule
            .onNodeWithContentDescription(menuButton)
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.my_games))
            .performClick()
        navController.assertCurrentRouteName(Screen.FAVORITES)
    }

    @Test
    fun freeToPlayNavHost_clickBackOnFavoritesScreen_navigatesToHomeScreen() {
        val menuButton = composeTestRule.activity.getString(R.string.menu_button)
        composeTestRule
            .onNodeWithContentDescription(menuButton)
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.my_games))
            .performClick()
        performNavigateUp()
        navController.assertCurrentRouteName(Screen.HOME)
    }

    @Test
    fun freeToPlayNavHost_clickSearchButton_navigatesToSearchScreen() {
        val searchButton = composeTestRule.activity.getString(R.string.search_button)
        composeTestRule
            .onNodeWithContentDescription(searchButton)
            .performClick()
        navController.assertCurrentRouteName(Screen.SEARCH)
    }

    @Test
    fun freeToPlayNavHost_clickBackOnSearchScreen_navigatesToHomeScreen() {
        val searchButton = composeTestRule.activity.getString(R.string.search_button)
        composeTestRule
            .onNodeWithContentDescription(searchButton)
            .performClick()
        performNavigateUp()
        navController.assertCurrentRouteName(Screen.HOME)
    }

    @Test
    fun freeToPlayNavHost_clickOnFirstGameFromList_navigatesToGameDetailsScreen() {
        composeTestRule.onNode(hasTestTag("Games"))
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeFreeToPlayAppDataSource.games.first().title))
            .performClick()
        navController.assertCurrentRouteName(Screen.GameDetails.ROUTE)
    }

    @Test
    fun freeToPlayNavHost_clickBackOnGameDetailsScreen_navigatesToHomeScreen() {
        composeTestRule.onNode(hasTestTag("Games"))
            .onChildren()
            .onFirst()
            .performClick()
        performNavigateUp()
        navController.assertCurrentRouteName(Screen.HOME)
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule
            .onNodeWithContentDescription(backText)
            .assertExists()
            .performClick()
    }

    // Optional to identify elements on the screen while tests are running (hybrid testing)
    private fun waitOnePlusSeconds(delayMillis: Long = 0L) {
        AsyncTimer.start(delayMillis)
        composeTestRule.waitUntil(delayMillis + 1000L) { AsyncTimer.expired }
    }
}