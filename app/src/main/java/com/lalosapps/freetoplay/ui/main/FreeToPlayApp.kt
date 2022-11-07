package com.lalosapps.freetoplay.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawer
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawerItem
import com.lalosapps.freetoplay.ui.screens.base.Screen
import com.lalosapps.freetoplay.ui.screens.home.HomeScreen
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun FreeToPlayApp(
    uiState: Resource<List<Game>>
) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
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
                        text = stringResource(R.string.all_games),
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.Window,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = stringResource(R.string.pc_games),
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.Web,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = stringResource(R.string.web_games),
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            // navigate later
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.TrendingUp,
                        iconColor = MaterialTheme.colors.onBackground,
                        text = stringResource(R.string.latest_games),
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
        NavHost(
            modifier = Modifier.padding(scaffoldPadding),
            navController = navController,
            startDestination = Screen.HOME
        ) {
            composable(Screen.HOME) {
                HomeScreen(
                    uiState = uiState,
                    onOpenDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    onSearch = {

                    },
                    onGameClick = {

                    }
                )
            }
        }
    }
}