package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lalosapps.freetoplay.domain.model.Game

@Composable
fun SearchSuggestions(
    games: List<Game>,
    onSuggestionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(games) { game ->
            SearchSuggestionItem(
                game = game,
                onItemClick = onSuggestionClick
            )
        }
    }
}