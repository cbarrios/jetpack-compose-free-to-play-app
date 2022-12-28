package com.lalosapps.freetoplay.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.domain.model.Game
import kotlinx.coroutines.launch
import com.lalosapps.freetoplay.R

@Composable
fun SearchDetails(
    games: List<Game>,
    onItemClick: (Int) -> Unit
) {
    if (games.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nothing_yet))
        }
    } else {
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
                    .testTag("GamesVerticalList"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(8.dp),
                state = listState
            ) {
                items(items = games, key = { it.id }) { game ->
                    SearchDetailsItem(
                        game = game,
                        onItemClick = onItemClick
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
}