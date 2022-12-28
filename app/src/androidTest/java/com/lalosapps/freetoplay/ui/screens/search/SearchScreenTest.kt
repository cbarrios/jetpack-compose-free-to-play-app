package com.lalosapps.freetoplay.ui.screens.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.lalosapps.freetoplay.domain.usecases.GetGamesFlowUseCase
import com.lalosapps.freetoplay.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalLifecycleComposeApi::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(GetGamesFlowUseCase(FakeGamesRepository()))
    }

    @Test
    fun searchScreen_onEmptyGames_verifyTopBar() {
        val emptyGames = FakeSearchScreenDataSource.emptyGames
        val topBarTitle = FakeSearchScreenDataSource.topBarTitle
        composeTestRule.setContent {
            SearchScreen(
                games = emptyGames,
                barTitle = topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        val backButtonContDesc = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule
            .onNodeWithContentDescription(backButtonContDesc)
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithTag("GameDetailsNavBarTitle")
            .assertIsDisplayed()
            .assertTextEquals(topBarTitle)

        composeTestRule
            .onNodeWithText(emptyGames.size.toString())
            .assertIsDisplayed()

        val filterButtonContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterButtonContDesc)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun searchScreen_onEmptyGames_clickOnFilterIcon_verifyAllGenresShown() {
        composeTestRule.setContent {
            SearchScreen(
                games = FakeSearchScreenDataSource.emptyGames,
                barTitle = FakeSearchScreenDataSource.topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        val filterButtonContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterButtonContDesc)
            .performClick()

        val genresList = composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())

        genresList.assertCountEquals(FakeSearchScreenDataSource.allGenres.size)

        FakeSearchScreenDataSource.allGenres.forEachIndexed { index, text ->
            genresList[index].assertTextEquals(text)
        }
    }

    @Test
    fun searchScreen_onEmptyGames_verifySearchBar() {
        val emptyGames = FakeSearchScreenDataSource.emptyGames
        val topBarTitle = FakeSearchScreenDataSource.topBarTitle
        composeTestRule.setContent {
            SearchScreen(
                games = emptyGames,
                barTitle = topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        val text = composeTestRule.activity.getString(R.string.search)
        composeTestRule
            .onNodeWithTag("SearchBar")
            .assertIsDisplayed()
            .assert(hasText(text))
            .assert(hasClickAction())
            .assert(hasImeAction(ImeAction.Search))

        val closeButtonContDesc = composeTestRule.activity.getString(R.string.clear_search_query)
        composeTestRule
            .onNodeWithContentDescription(closeButtonContDesc)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun searchScreen_onEmptyGames_verifyNothingYetTextShown() {
        composeTestRule.setContent {
            SearchScreen(
                games = FakeSearchScreenDataSource.emptyGames,
                barTitle = FakeSearchScreenDataSource.topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        val text = composeTestRule.activity.getString(R.string.nothing_yet)
        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_onManyGames_verifyGamesVerticalListAndScrollUpButton() {
        val games = FakeSearchScreenDataSource.manyGames
        composeTestRule.setContent {
            SearchScreen(
                games = games,
                barTitle = FakeSearchScreenDataSource.topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        val list = composeTestRule.onNodeWithTag("GamesVerticalList")

        list.assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .onFirst()
            .assert(hasText(games.first().title))

        list.performScrollToIndex(games.lastIndex)

        val scrollUpButtonContDesc =
            composeTestRule.activity.getString(R.string.scroll_up_content_description)
        composeTestRule
            .onNodeWithContentDescription(scrollUpButtonContDesc)
            .assertIsDisplayed()

        list.performScrollToIndex(0)

        composeTestRule
            .onNodeWithContentDescription(scrollUpButtonContDesc)
            .assertDoesNotExist()
    }

    @Test
    fun searchScreen_onManyGames_performSimpleSearch_verifySearchSuggestions() {
        val games = FakeSearchScreenDataSource.manyGames
        composeTestRule.setContent {
            SearchScreen(
                games = games,
                barTitle = FakeSearchScreenDataSource.topBarTitle,
                onBackPress = { },
                onItemClick = { },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performTextInput("a")

        composeTestRule
            .onNodeWithTag("SearchSuggestions")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(games.size)
    }
}