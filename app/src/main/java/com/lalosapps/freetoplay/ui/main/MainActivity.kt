package com.lalosapps.freetoplay.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lalosapps.freetoplay.ui.theme.FreeToPlayTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalLifecycleComposeApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.splashScreenVisible }
        }
        setContent {
            FreeToPlayTheme {
                val uiState = viewModel.games.collectAsStateWithLifecycle().value
                FreeToPlayApp(uiState = uiState)
            }
        }
    }
}