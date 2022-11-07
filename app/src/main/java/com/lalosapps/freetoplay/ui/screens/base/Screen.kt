package com.lalosapps.freetoplay.ui.screens.base

object Screen {

    const val HOME = "home"

    object GameDetails {
        private const val GAME_DETAILS = "gameDetails"
        const val A1_INT_GAME_ID = "gameId"
        const val ROUTE = "$GAME_DETAILS/{$A1_INT_GAME_ID}"
        fun createRoute(gameId: Int): String {
            return "$GAME_DETAILS/$gameId"
        }
    }
}
