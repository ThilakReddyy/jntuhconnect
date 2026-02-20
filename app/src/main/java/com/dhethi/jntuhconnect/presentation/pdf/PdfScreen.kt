package com.dhethi.jntuhconnect.presentation.pdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PdfScreen(
    viewModel: PdfViewModel = hiltViewModel(),
    url: String
) {
    val state = viewModel.state.value

    LaunchedEffect(url) {
        viewModel.loadPdf(url)
    }

    when {
        state.isLoading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        state.error != null -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: ${state.error}")
        }

        else -> {
            // Vertical scrollable list of pages
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                items(state.bitmaps.size) { index ->
                    ZoomablePdfPage(
                        bitmap = state.bitmaps[index].asImageBitmap(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ZoomablePdfPage(
    bitmap: androidx.compose.ui.graphics.ImageBitmap,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }

    val state = remember {
        TransformableState { zoomChange, _, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)
        }
    }

    // Using pointerInput to allow zoom without blocking vertical scroll
    Box(
        modifier = modifier
            .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
            .clipToBounds()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .transformable(state)
            .then(if (scale == 1f) Modifier else Modifier), // Keeps scroll active when not zoomed
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

