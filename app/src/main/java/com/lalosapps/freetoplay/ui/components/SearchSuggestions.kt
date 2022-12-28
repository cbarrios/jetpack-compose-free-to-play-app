package com.lalosapps.freetoplay.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.R
import kotlinx.coroutines.launch

@Composable
fun SearchSuggestions(
    games: List<Game>,
    onSuggestionClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showUpButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("SearchSuggestions"),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(items = games, key = { it.id }) { game ->
                SearchSuggestionItem(
                    game = game,
                    onItemClick = onSuggestionClick
                )
            }
        }
        AnimatedVisibility(
            visible = showUpButton,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = { scope.launch { listState.scrollToItem(0) } }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = stringResource(id = R.string.scroll_up_content_description)
                )
            }
        }
    }
}