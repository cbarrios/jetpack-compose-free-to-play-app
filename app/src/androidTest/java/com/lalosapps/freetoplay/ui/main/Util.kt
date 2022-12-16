package com.lalosapps.freetoplay.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.navigation.NavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
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

// Optional method to wait so that we can identify elements on the screen while tests are running (hybrid testing)
inline fun <reified A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.waitOnePlusSeconds(
    delayMillis: Long = 0L
) {
    AsyncTimer.start(delayMillis)
    this.waitUntil(delayMillis + 1000L) { AsyncTimer.expired }
}