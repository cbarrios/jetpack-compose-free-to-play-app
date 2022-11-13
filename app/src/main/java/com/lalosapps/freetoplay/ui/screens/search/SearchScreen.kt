package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.*

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
    val allGenres = viewModel.allGenres.collectAsStateWithLifecycle().value
    Column(modifier = Modifier.fillMaxSize()) {
        GameDetailsNavBar(
            title = barTitle,
            onBackPress = onBackPress,
            trailingIcon = {
                Spacer(modifier = Modifier.weight(1f))
                IconToggleButton(
                    checked = viewModel.showFilter,
                    onCheckedChange = viewModel::onFilterToggle
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = if (viewModel.showFilter) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                    )
                }
            }
        )
        AnimatedChipRow(
            visible = viewModel.showFilter,
            data = allGenres,
            onChipClick = viewModel::filterByGenre
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