package com.lalosapps.freetoplay.ui.main

import androidx.navigation.NavController
import org.junit.Assert.assertEquals
import java.util.Timer
import kotlin.concurrent.schedule

fun NavController.assertCurrentRouteName(expectedRoute: String) {
    assertEquals(expectedRoute, currentBackStackEntry?.destination?.route)
}

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}