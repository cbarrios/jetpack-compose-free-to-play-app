package com.lalosapps.freetoplay.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.ui.main.fake.FakeFreeToPlayAppDataSource
import com.lalosapps.freetoplay.ui.screens.base.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FreeToPlayAppNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
    @Before
    fun setupFreeToPlayNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            FreeToPlayApp(
                uiState = FakeFreeToPlayAppDataSource.uiState,
                gamesList = FakeFreeToPlayAppDataSource.games,
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
        navController.assertCurrentRouteName(Screen.HOME)
    }

}