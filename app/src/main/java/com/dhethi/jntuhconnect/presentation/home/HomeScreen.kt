package com.dhethi.jntuhconnect.presentation.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.HistoryToggleOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.presentation.Screen
import com.dhethi.jntuhconnect.presentation.components.ROLL_NUMBER_LENGTH
import com.dhethi.jntuhconnect.presentation.components.RollInputSheet
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.explore.ToolAction
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.explore.homeQuickTools
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.brandGradient

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenStudent: (String) -> Unit,
    onOpenStudentTab: (String, String) -> Unit,
    onOpenRoute: (String) -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    var roll by rememberSaveable { mutableStateOf("") }

    // A roll-input sheet used when a quick tool needs a roll number.
    var pendingTab by remember { mutableStateOf<String?>(null) }
    pendingTab?.let { tab ->
        RollInputSheet(
            title = "Enter roll number",
            onDismiss = { pendingTab = null },
            onSubmit = { r ->
                pendingTab = null
                onOpenStudentTab(r, tab)
            }
        )
    }

    fun submitSearch() {
        when {
            roll.isBlank() ->
                Toast.makeText(context, R.string.error_roll_empty, Toast.LENGTH_SHORT).show()

            roll.length != ROLL_NUMBER_LENGTH ->
                Toast.makeText(context, R.string.error_roll_length, Toast.LENGTH_SHORT).show()

            else -> {
                keyboard?.hide()
                onOpenStudent(roll)
            }
        }
    }

    fun onTool(tool: ToolItem) {
        when (val action = tool.action) {
            is ToolAction.StudentTab -> pendingTab = action.tab
            is ToolAction.Route -> onOpenRoute(action.route)
            is ToolAction.External -> openCustomTab(context, action.url)
        }
    }

    val listState = rememberLazyListState()
    val heroScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex >= 1 } }

    Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Dimens.spaceXxl)
    ) {
        item {
            HomeHero(
                rollValue = roll,
                onRollChange = { roll = it },
                onSubmit = { submitSearch() }
            )
        }

        item {
            SectionHeader(
                title = "Quick tools",
                icon = Icons.AutoMirrored.Rounded.TrendingUp,
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.space)
            )
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = Dimens.space),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
            ) {
                items(homeQuickTools) { tool ->
                    QuickToolCard(tool = tool, onClick = { onTool(tool) })
                }
            }
        }

        // Recent searches
        item {
            Spacer(Modifier.height(Dimens.spaceLg))
            SectionHeader(
                title = "Recent searches",
                icon = Icons.Rounded.HistoryToggleOff,
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
                actionText = if (state.students.isNotEmpty()) "Clear" else null,
                onActionClick = if (state.students.isNotEmpty()) viewModel::deleteAllStudents else null
            )
        }
        if (state.students.isEmpty()) {
            item { RecentEmpty() }
        } else {
            items(state.students, key = { it.rollNumber }) { student ->
                RecentStudentCard(
                    student = student,
                    onClick = { onOpenStudent(student.rollNumber) },
                    modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceXs)
                )
            }
        }

        // Latest updates
        if (state.latestUpdates.isNotEmpty()) {
            item {
                Spacer(Modifier.height(Dimens.spaceLg))
                SectionHeader(
                    title = "Latest updates",
                    icon = Icons.Rounded.HistoryToggleOff,
                    actionText = "See all",
                    onActionClick = { onOpenRoute(Screen.Updates.route) },
                    modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm)
                )
            }
            items(state.latestUpdates) { update ->
                UpdatePreviewCard(
                    update = update,
                    onClick = { openCustomTab(context, update.link) },
                    modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceXs)
                )
            }
        }
    }
        StatusBarScrim(visible = heroScrolled)
    }
}

@Composable
private fun HomeHero(
    rollValue: String,
    onRollChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brandGradient(dark))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Dimens.spaceLg)
                .padding(top = Dimens.space, bottom = Dimens.spaceXl)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(Dimens.spaceSm))
                Text(
                    "JNTUH Connect",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(Dimens.spaceXl))
            Text(
                "Check your results",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Results, backlogs, credits & more — in one tap.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(Dimens.space))
            HeroSearchBar(
                value = rollValue,
                onValueChange = onRollChange,
                onSubmit = onSubmit
            )
        }
    }
}

@Composable
private fun RecentEmpty() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space, vertical = Dimens.space),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No recent searches yet",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Dimens.spaceXs))
        Text(
            "Search a roll number above to see it here.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
