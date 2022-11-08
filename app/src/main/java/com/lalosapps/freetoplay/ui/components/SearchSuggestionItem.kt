package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.domain.model.Game

@Composable
fun SearchSuggestionItem(
    game: Game,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onItemClick(game.id) }
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = game.title,
            color = MaterialTheme.colors.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}