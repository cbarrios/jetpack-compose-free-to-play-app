package com.lalosapps.freetoplay.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawer
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawerItem

@Composable
fun FreeToPlayApp(
    uiState: Resource<List<Game>>
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerShape = RectangleShape,
        drawerContent = {
            NavigationDrawer(
                header = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_free_to_play_splash),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                            contentDescription = null
                        )
                    }
                },
                content = {
                    NavigationDrawerItem(
                        icon = Icons.Default.Games,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = "All Games",
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.Window,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = "PC Games",
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.Web,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = "Web Games",
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.TrendingUp,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = "Latest Games",
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                }
            )
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                Resource.Loading -> Text(text = "Loading")
                is Resource.Error -> Text(text = "Error: ${uiState.error ?: uiState.data}")
                is Resource.Success -> Text(text = "Success: ${uiState.data.size}")
            }
        }
    }
}