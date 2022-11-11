package com.lalosapps.freetoplay.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.ui.components.GameDetailsNavBar
import com.lalosapps.freetoplay.ui.components.SearchDetails
import com.lalosapps.freetoplay.R

@ExperimentalLifecycleComposeApi
@Composable
fun FavoritesScreen(
    onBackPress: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites = viewModel.favorites.collectAsStateWithLifecycle().value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GameDetailsNavBar(
            title = stringResource(id = R.string.my_games),
            onBackPress = onBackPress
        )
        if (favorites.isNotEmpty()) {
            SearchDetails(
                games = favorites,
                onItemClick = onItemClick
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.nothing_yet),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}