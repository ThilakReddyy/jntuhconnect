package com.dhethi.jntuhconnect.presentation.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.presentation.components.ErrorScreen
import com.dhethi.jntuhconnect.presentation.components.SmallRoundedButton
import com.dhethi.jntuhconnect.presentation.components.openCustomTab

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
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Latest Updates",
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Stay informed with academic announcements",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Tab Buttons
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            options.forEach { option ->
                SmallRoundedButton(
                    option,
                    containerColor = if (uiState.currentTab == option) Color.Black else Color.White,
                    textColor = if (uiState.currentTab == option) Color.White else Color.DarkGray,
                    onClick = { viewModel.switchCurrentTab(option) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Notifications list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.currentTabUpdates) { update ->
                ResultCard(
                    title = update.title,
                    description = "",
                    date = update.date,
                    category = "Academic",
                    url = update.link
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
@Preview
fun EmptyCardCreative() {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("No Latest Updates", color = Color.LightGray, fontSize = 16.sp)
    }

}

@Composable
fun ResultCard(
    title: String,
    description: String,
    date: String,
    category: String = "Academic",
    url: String
) {

    val context = LocalContext.current
    Box(
        modifier = Modifier

            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable() {
                openCustomTab(context, url)
            }
            .padding(16.dp)
            .heightIn(min = 120.dp)


    ) {
        Column(modifier = Modifier) {
            Text(
                title, fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
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

            Box(modifier = Modifier.fillMaxSize()) {
                // Date
                Text(
                    text = date.replace("-", " "),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                // Category chip
                Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Text(
                        text = category,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                Color.LightGray,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}


