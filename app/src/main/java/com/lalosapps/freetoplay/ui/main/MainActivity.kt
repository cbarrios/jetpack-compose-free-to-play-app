package com.lalosapps.freetoplay.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lalosapps.freetoplay.core.util.Resource
import com.lalosapps.freetoplay.ui.theme.FreeToPlayTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLifecycleComposeApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeToPlayTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState = viewModel.games.collectAsStateWithLifecycle().value
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when (uiState) {
                            Resource.Loading -> Text(text = "Loading")
                            is Resource.Error -> Text(text = "Error: ${uiState.error ?: uiState.data}")
                            is Resource.Success -> Text(text = "Success: ${uiState.data.size}")
                        }
                    }
                }
            }
        }
    }
}