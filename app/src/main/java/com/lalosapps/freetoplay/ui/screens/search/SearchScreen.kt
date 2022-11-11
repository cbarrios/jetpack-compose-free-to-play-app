package com.lalosapps.freetoplay.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        AnimatedVisibility(visible = viewModel.showFilter) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(allGenres, key = { it }) {
                    var toggled by rememberSaveable { mutableStateOf(false) }
                    Chip(
                        borderWidth = 1.dp,
                        modifier = Modifier.clickable {
                            toggled = !toggled
                            viewModel.filterByGenre(it)
                        },
                        backgroundColor = if (toggled) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                        borderColor = if (toggled) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = if (toggled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
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