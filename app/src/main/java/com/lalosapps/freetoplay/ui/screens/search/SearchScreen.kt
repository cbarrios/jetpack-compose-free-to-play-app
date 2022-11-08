package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.GameDetailsNavBar
import com.lalosapps.freetoplay.ui.components.SearchBar
import com.lalosapps.freetoplay.ui.components.SearchDetails
import com.lalosapps.freetoplay.ui.components.SearchSuggestions

@ExperimentalLifecycleComposeApi
@Composable
fun SearchScreen(
    games: List<Game>,
    barTitle: String,
    onBackPress: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = games) {
        if (games != viewModel.originalList) {
            viewModel.setGames(games)
        }
    }

    val uiState = viewModel.games.collectAsStateWithLifecycle().value
    Column(modifier = Modifier.fillMaxSize()) {
        GameDetailsNavBar(
            title = barTitle,
            onBackPress = onBackPress
        )
        SearchBar(
            query = viewModel.query,
            onQueryChange = viewModel::onQueryChange,
            onQueryClear = viewModel::onQueryClear,
            onSearch = viewModel::showSearchResults
        )
        if (!viewModel.showSearch) {
            SearchSuggestions(
                games = uiState,
                onSuggestionClick = {
                    onItemClick(it)
                }
            )
        } else {
            SearchDetails(
                games = uiState,
                onItemClick = onItemClick
            )
        }
    }
}