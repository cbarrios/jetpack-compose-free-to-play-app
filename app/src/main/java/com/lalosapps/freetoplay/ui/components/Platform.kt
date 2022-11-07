package com.lalosapps.freetoplay.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Window
import androidx.compose.runtime.Composable

@Composable
fun Platform(
    platform: String
) {
    val resource = when {
        platform.contains("windows", ignoreCase = true) -> Icons.Default.Window
        else -> Icons.Default.Web
    }

    Icon(
        imageVector = resource,
        contentDescription = resource.name,
        tint = MaterialTheme.colors.primary
    )
}