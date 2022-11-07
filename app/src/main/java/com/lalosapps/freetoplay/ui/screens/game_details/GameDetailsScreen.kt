package com.lalosapps.freetoplay.ui.screens.game_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.GameDetails

@ExperimentalLifecycleComposeApi
@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    when (val uiState = viewModel.game.collectAsStateWithLifecycle().value) {
        Resource.Loading -> LoadingScreen()
        is Resource.Error -> ErrorScreen(uiState.error.toString())
        is Resource.Success -> {
            GameDetails(gameDetails = uiState.data)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message")
    }
}

@Composable
fun GameDetails(gameDetails: GameDetails) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = gameDetails.toString())
    }
}