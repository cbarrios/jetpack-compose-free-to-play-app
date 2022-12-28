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

        composeTestRule
            .onNodeWithText(games.size.toString())
            .assertIsDisplayed()

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

    @Test
    fun searchScreen_onManyGames_performSearchWithNoResults_thenClickOnClearSearchIcon_verifyGameCounterIsZeroAndNothingYetTextShownThenAfterClearingGamesAreShown() {
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
            .performTextInput("zzz")

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performImeAction()

        composeTestRule
            .onNodeWithText("0")
            .assertIsDisplayed()

        val text = composeTestRule.activity.getString(R.string.nothing_yet)
        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()

        val clearButtonContDesc = composeTestRule.activity.getString(R.string.clear_search_query)
        composeTestRule
            .onNodeWithContentDescription(clearButtonContDesc)
            .performClick()

        composeTestRule
            .onNodeWithTag("GamesVerticalList")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(games.size.toString())
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_onManyGames_performFilterByCardGame_thenClickOnFilterIconAgain_verifyOnlyCardGamesShownThenAllGamesAreShownAgain() {
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

        val filterIconContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Card Game"))
            .performClick()

        composeTestRule
            .onNodeWithTag("GamesVerticalList")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(FakeSearchScreenDataSource.cardGames.size)

        composeTestRule
            .onNodeWithText(FakeSearchScreenDataSource.cardGames.size.toString())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        composeTestRule
            .onNodeWithText(games.size.toString())
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_onManyGames_performFilterByCardGame_thenSearchForTitans2_verifyOnlyTheGameWithTitleTitans2IsShown() {
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

        val filterIconContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Card Game"))
            .performClick()

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performTextInput("Titans 2")

        composeTestRule
            .onNodeWithTag("SearchSuggestions")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeSearchScreenDataSource.cardGames.last().title))
    }

    @Test
    fun searchScreen_onManyGames_performFilterByCardGame_thenSearchForTitans4AndClickSearchImeAction_thenPerformFilterByStrategy_verifyNoGamesAreShownThenAfterClickingOnStrategyTheListWithOnlyThatGameIsShown() {
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

        val filterIconContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Card Game"))
            .performClick()

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performTextInput("Titans 4")

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performImeAction()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.nothing_yet))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Strategy"))
            .performClick()

        composeTestRule
            .onNodeWithTag("GamesVerticalList")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeSearchScreenDataSource.strategyGames.first().title))
    }

    @Test
    fun searchScreen_onManyGames_performFilterByCardGame_thenSearchForTitans4_thenPerformFilterByStrategy_verifyNoSuggestionsShownThenAfterClickingOnStrategyTitans4IsTheOnlyGameSuggestion() {
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

        val filterIconContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Card Game"))
            .performClick()

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performTextInput("Titans 4")

        composeTestRule
            .onNodeWithTag("SearchSuggestions")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(0)

        composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Strategy"))
            .performClick()

        composeTestRule
            .onNodeWithTag("SearchSuggestions")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(1)
            .onFirst()
            .assert(hasText(FakeSearchScreenDataSource.strategyGames.first().title))
    }

    @Test
    fun searchScreen_onManyGames_performFilterByStrategyAndSports_thenClickOnThoseGenresAgain_verifyOnlyThoseGamesAreShownThenAllGamesAreShownAgain() {
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

        val filterIconContDesc =
            composeTestRule.activity.getString(R.string.filter_games_content_description)
        composeTestRule
            .onNodeWithContentDescription(filterIconContDesc)
            .performClick()

        val strategyChip = composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Strategy"))
        strategyChip.performClick()

        val sportsChip = composeTestRule
            .onNodeWithTag("GenresHorizontalList")
            .onChildren()
            .filter(hasClickAction())
            .filterToOne(hasText("Sports"))
        sportsChip.performClick()

        composeTestRule
            .onNodeWithTag("GamesVerticalList")
            .assertIsDisplayed()
            .onChildren()
            .filter(hasClickAction())
            .assertCountEquals(FakeSearchScreenDataSource.strategyGames.size + FakeSearchScreenDataSource.sportsGames.size)

        composeTestRule
            .onNodeWithText("${FakeSearchScreenDataSource.strategyGames.size + FakeSearchScreenDataSource.sportsGames.size}")
            .assertIsDisplayed()

        strategyChip.performClick()
        sportsChip.performClick()

        composeTestRule
            .onNodeWithText(games.size.toString())
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_onManyGames_performSimpleSearch_thenPerformEmptySearch_verifySuggestionsShownThenAllGamesShownAgain() {
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

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performTextInput("")

        composeTestRule
            .onNodeWithTag("SearchSuggestions")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("GamesVerticalList")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(games.size.toString())
            .assertIsDisplayed()
    }
}