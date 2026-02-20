package com.dhethi.jntuhconnect.presentation.updates

import android.graphics.BlurMaskFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.presentation.components.ErrorScreen
import com.dhethi.jntuhconnect.presentation.components.SmallRoundedButton
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatesScreen(
    viewModel: UpdatesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState = viewModel.state.value
    val options = Constants.UPDATES_TABS
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        Spacer(modifier = Modifier.height(12.dp))


        // Tab Buttons
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
        ) {
            options.forEach { option ->
                SmallRoundedButton(
                    option,
                    containerColor = if (uiState.currentTab == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    textColor = if (uiState.currentTab == option) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.switchCurrentTab(option) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Notifications list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.currentTabUpdates) { update ->
                val releaseDateString = update.releaseDate
                val today = LocalDate.now()

                ResultCard(
                    title = update.title,
                    description = "",
                    date = update.date,
                    category = "Academic",
                    url = update.link,
                    imp = releaseDateString == today.toString()
                )
            }

            // Show loading indicator at the bottom when fetching next page
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
            }

            // Show "No updates" if empty
            if (!uiState.isLoading && uiState.currentTabUpdates.isEmpty()) {
                item {
                    EmptyCardCreative()
                }
            }
        }

        // Error message overlay
        if (uiState.error.isNotEmpty()) {
            ErrorScreen(uiState.error, refreshPage = { /* You can reload */ })
        }
    }

    // Infinite scroll: load next page when near bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= totalItems - 3) {
                    viewModel.loadNextPage()
                }
            }
    }
}

@Composable
fun EmptyCardCreative() {

    Row(
        modifier = Modifier.fillMaxSize()
            .padding(0.dp,240.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

    ) {

        Text("No Latest Updates", color = MaterialTheme.colorScheme.secondary, fontSize = 16.sp)
    }

}

@Composable
fun ResultCard(
    title: String,
    description: String,
    date: String,
    category: String = "Academic",
    url: String,
    imp: Boolean = false
) {

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openCustomTab(context, url) }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .heightIn(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                title, fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                description,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                // Date
                Text(
                    text = date.replace("-", " "),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        if (imp) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp)
                    .size(16.dp)
                    .drawBehind {
                        // Shadow paint with blur
                        val paint = Paint().asFrameworkPaint().apply {
                            color = "#403D3D3D".toColorInt() // 25% alpha gray
                            maskFilter = BlurMaskFilter(4.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
                        }
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.drawRoundRect(
                                -2.dp.toPx(), // X offset = -2.dp
                                0f,           // Y offset = 0.dp
                                size.width - 2.dp.toPx(),
                                size.height,
                                4.dp.toPx(),  // corner radius
                                4.dp.toPx(),
                                paint
                            )
                        }
                    }
                    .background(Color.Red)
            )


        }
    }

}



