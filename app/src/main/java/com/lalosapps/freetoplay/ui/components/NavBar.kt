package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.R

@Composable
fun GameDetailsNavBar(
    title: String,
    onBackPress: () -> Unit,
    badge: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackPress) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_button),
                tint = MaterialTheme.colors.onBackground
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = if (badge == null && trailingIcon == null)
                Modifier.testTag("GameDetailsNavBarTitle")
            else
                Modifier
                    .widthIn(max = 150.dp)
                    .testTag("GameDetailsNavBarTitle"),
            text = title,
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        badge?.invoke()
        trailingIcon?.invoke()
    }
}