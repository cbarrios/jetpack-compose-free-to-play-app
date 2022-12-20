package com.lalosapps.freetoplay.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lalosapps.freetoplay.ui.screens.base.ChipData

@Composable
fun AnimatedChipRow(
    visible: Boolean,
    data: List<ChipData>,
    onChipClick: (String) -> Unit
) {
    Column {
        AnimatedVisibility(visible = visible) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("GenresHorizontalList"),
                contentPadding = PaddingValues(bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(data, key = { it.text }) {
                    Chip(
                        borderWidth = 1.dp,
                        modifier = Modifier.clickable {
                            onChipClick(it.text)
                        },
                        backgroundColor = if (it.checked) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                        borderColor = if (it.checked) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                    ) {
                        Text(
                            text = it.text,
                            style = MaterialTheme.typography.caption,
                            color = if (it.checked) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}