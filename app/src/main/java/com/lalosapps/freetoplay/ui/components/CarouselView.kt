package com.lalosapps.freetoplay.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@ExperimentalPagerApi
@Composable
fun CarouselView(
    urls: List<String>,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    pagerIndicatorColor: Color = MaterialTheme.colors.primary,
    shape: Shape = RectangleShape,
    crossFade: Int? = null,
    clickable: Boolean = false,
    onImageClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        val pagerState = rememberPagerState(0)
        LaunchedEffect(key1 = Unit) {
            while (true) {
                delay(5000)
                var next = pagerState.currentPage + 1
                if (next == pagerState.pageCount) next = 0
                pagerState.animateScrollToPage(page = next)
            }
        }
        HorizontalPager(
            count = urls.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(shape = shape)
        ) { index ->
            NetworkImage(
                url = urls[index],
                crossFade = crossFade,
                contentScale = contentScale,
                modifier = if (clickable) Modifier
                    .fillMaxSize()
                    .clickable {
                        onImageClick(urls[index])
                    } else Modifier.fillMaxSize(),
                onLoading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                onError = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            )
        }
        HorizontalPagerIndicator(
            activeColor = pagerIndicatorColor,
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(color = Color.Transparent)
                .padding(bottom = 5.dp)
        )
    }
}