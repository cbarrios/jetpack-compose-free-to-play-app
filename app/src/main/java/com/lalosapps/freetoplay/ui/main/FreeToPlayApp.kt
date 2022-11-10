package com.lalosapps.freetoplay.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawer
import com.lalosapps.freetoplay.R
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.ui.components.drawer.NavigationDrawerItem
import com.lalosapps.freetoplay.ui.screens.base.Screen
import com.lalosapps.freetoplay.ui.screens.favorites.FavoritesScreen
import com.lalosapps.freetoplay.ui.screens.game_details.GameDetailsScreen
import com.lalosapps.freetoplay.ui.screens.home.HomeScreen
import com.lalosapps.freetoplay.ui.screens.search.SearchScreen
import kotlinx.coroutines.launch

@ExperimentalLifecycleComposeApi
@ExperimentalPagerApi
@Composable
fun FreeToPlayApp(
    uiState: Resource<List<Game>>,
    gamesList: List<Game>,
    onDrawerAllGamesClick: () -> Unit,
    onDrawerPcGamesClick: () -> Unit,
    onDrawerWebGamesClick: () -> Unit,
    onDrawerLatestGamesClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    if (scaffoldState.drawerState.isOpen) {
        BackHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }

    val defaultTitle = stringResource(id = R.string.app_name)
    var barTitle by rememberSaveable { mutableStateOf(defaultTitle) }
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
                        iconColor = MaterialTheme.colors.primary,
                        text = stringResource(R.string.all_games),
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            barTitle = defaultTitle
                            onDrawerAllGamesClick()
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                    val pcGames = stringResource(R.string.pc_games)
                    NavigationDrawerItem(
                        icon = Icons.Default.Window,
                        iconColor = MaterialTheme.colors.primary,
                        text = pcGames,
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            barTitle = pcGames
                            onDrawerPcGamesClick()
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                    val webGames = stringResource(R.string.web_games)
                    NavigationDrawerItem(
                        icon = Icons.Default.Web,
                        iconColor = MaterialTheme.colors.primary,
                        text = webGames,
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            barTitle = webGames
                            onDrawerWebGamesClick()
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                    val latestGames = stringResource(R.string.latest_games)
                    NavigationDrawerItem(
                        icon = Icons.Default.TrendingUp,
                        iconColor = MaterialTheme.colors.primary,
                        text = latestGames,
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            barTitle = latestGames
                            onDrawerLatestGamesClick()
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        icon = Icons.Default.Favorite,
                        iconColor = MaterialTheme.colors.primary,
                        text = stringResource(R.string.my_games),
                        textStyle = MaterialTheme.typography.body1,
                        textColor = MaterialTheme.colors.onBackground,
                        onClick = {
                            navController.navigate(Screen.FAVORITES) {
                                launchSingleTop = true
                            }
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
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
                    games = gamesList,
                    scaffoldState = scaffoldState,
                    barTitle = barTitle,
                    onOpenDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    onSearch = {
                        navController.navigate(Screen.SEARCH) {
                            launchSingleTop = true
                        }
                    },
                    onGameClick = { gameId ->
                        navController.navigate(Screen.GameDetails.createRoute(gameId))
                    }
                )
            }
            composable(
                route = Screen.GameDetails.ROUTE,
                arguments = listOf(
                    navArgument(Screen.GameDetails.A1_INT_GAME_ID) {
                        type = NavType.IntType
                    }
                )
            ) {
                GameDetailsScreen(
                    onBackPress = { navController.navigateUp() },
                    onGameUrlClick = {
                        uriHandler.openUri(it)
                    }
                )
            }
            composable(Screen.SEARCH) {
                SearchScreen(
                    games = gamesList,
                    barTitle = barTitle,
                    onBackPress = { navController.navigateUp() },
                    onItemClick = { gameId ->
                        navController.navigate(Screen.GameDetails.createRoute(gameId))
                    }
                )
            }
            composable(Screen.FAVORITES) {
                FavoritesScreen(
                    onBackPress = { navController.navigateUp() },
                    onItemClick = { gameId ->
                        navController.navigate(Screen.GameDetails.createRoute(gameId))
                    }
                )
            }
        }
    }
}