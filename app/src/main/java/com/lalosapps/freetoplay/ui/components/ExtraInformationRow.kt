package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameDetailsExtraInfo(
    firstTitle: String,
    secondTitle: String,
    textColor: Color,
    icon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = firstTitle,
            style = MaterialTheme.typography.caption,
            color = textColor
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                it()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = secondTitle,
                style = MaterialTheme.typography.caption,
                color = textColor
            )
        }
    }
}