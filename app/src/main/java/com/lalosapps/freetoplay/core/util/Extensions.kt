package com.lalosapps.freetoplay.core.util

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import com.lalosapps.freetoplay.domain.model.Game

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        content = content
    )
}

fun List<Game>.getRandomUrls(): List<String> {
    return takeRandomElements(3).map {
        it.thumbnail
    }
}

fun <T> List<T>.takeRandomElements(numberOfElements: Int): List<T> {
    return if (numberOfElements > size) this else {
        asSequence().shuffled().take(numberOfElements).toList()
    }
}