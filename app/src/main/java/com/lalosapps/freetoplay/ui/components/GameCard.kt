package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.domain.model.Game

@Composable
fun GameCard(
    game: Game,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = if (game.isFavorite) {
            modifier
                .padding(8.dp)
                .clickable { onClick() }
                .shadow(
                    elevation = 16.dp,
                    shape = MaterialTheme.shapes.small,
                    spotColor = MaterialTheme.colors.primary
                )
        } else {
            modifier
                .padding(8.dp)
                .clickable { onClick() }
        },
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface,
        border = if (game.isFavorite) BorderStroke(Dp.Hairline, MaterialTheme.colors.primary) else null
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            NetworkImage(
                url = game.thumbnail,
                contentScale = ContentScale.Crop,
                crossFade = 1000,
                modifier = Modifier.fillMaxHeight(),
                onLoading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                onError = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = game.title,
                    modifier = Modifier.padding(5.dp),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = game.shortDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp)
                ) {
                    Chip(
                        borderWidth = 1.dp
                    ) {
                        Text(
                            text = game.genre,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Platform(platform = game.platform)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}