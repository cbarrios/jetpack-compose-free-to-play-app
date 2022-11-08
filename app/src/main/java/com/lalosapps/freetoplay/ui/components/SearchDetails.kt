package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.domain.model.Game

@Composable
fun SearchDetails(
    games: List<Game>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(games) { game ->
            SearchDetailsItem(
                game = game,
                onItemClick = onItemClick
            )
        }
    }

}