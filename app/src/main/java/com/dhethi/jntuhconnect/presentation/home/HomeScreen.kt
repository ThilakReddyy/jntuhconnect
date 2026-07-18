package com.dhethi.jntuhconnect.presentation.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.core.view.WindowCompat
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.presentation.components.RollInputSheet
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.isValidRollNumber
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.explore.ToolAction
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.explore.homeQuickTools
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.LocalJntuhDarkTheme
import com.dhethi.jntuhconnect.presentation.theme.brandGradient

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
    var searchAttempted by rememberSaveable { mutableStateOf(false) }
    val rollError = when {
        !searchAttempted -> null
        roll.isBlank() -> stringResource(R.string.error_roll_empty)
        !isValidRollNumber(roll) -> stringResource(R.string.error_roll_length)
        else -> null
    }

    var pendingTab by remember { mutableStateOf<String?>(null) }
    pendingTab?.let { tab ->
        RollInputSheet(
            title = "Enter roll number",
            onDismiss = { pendingTab = null },
            onSubmit = { submittedRoll ->
                pendingTab = null
                onOpenStudentTab(submittedRoll, tab)
            }
        )
    }

    fun submitSearch() {
        searchAttempted = true
        if (isValidRollNumber(roll)) {
            keyboard?.hide()
            onOpenStudent(roll)
        }
    }

    fun onTool(tool: ToolItem) {
        when (val action = tool.action) {
            is ToolAction.StudentTab -> pendingTab = action.tab
            is ToolAction.Route -> onOpenRoute(action.route)
            is ToolAction.External -> openCustomTab(context, action.url)
        }
    }

    val dark = LocalJntuhDarkTheme.current
    val listState = rememberLazyListState()
    // Protect the status-bar inset as soon as scrolling starts, rather than waiting
    // until the entire hero has left the viewport.
    val heroScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }
    val homeBackground = if (dark) Color(0xFF08090A) else Color(0xFFF7F7FA)
    val view = LocalView.current

    // The hero is dark in both modes, so its edge-to-edge status icons must stay light.
    DisposableEffect(view, dark, heroScrolled) {
        if (!view.isInEditMode) {
            val controller = WindowCompat.getInsetsController((view.context as Activity).window, view)
            controller.isAppearanceLightStatusBars = heroScrolled && !dark
        }
        onDispose {
            if (!view.isInEditMode) {
                WindowCompat.getInsetsController((view.context as Activity).window, view)
                    .isAppearanceLightStatusBars = !dark
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(homeBackground)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(homeBackground),
            contentPadding = PaddingValues(bottom = Dimens.spaceXxl)
        ) {
            item {
                HomeHero(
                    rollValue = roll,
                    onRollChange = { roll = it },
                    onSubmit = ::submitSearch,
                    error = rollError
                )
            }

            if (state.latestUpdates.isNotEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Notifications",
                        subtitle = "Released today or yesterday",
                        modifier = Modifier.padding(
                            start = Dimens.space,
                            end = Dimens.space,
                            top = Dimens.spaceXl,
                            bottom = Dimens.spaceMd
                        )
                    )
                }
                items(state.latestUpdates) { update ->
                    UpdatePreviewCard(
                        update = update,
                        onClick = { openCustomTab(context, update.link) },
                        modifier = Modifier.padding(
                            horizontal = Dimens.space,
                            vertical = Dimens.spaceXs
                        )
                    )
                }
            }

            if (state.recentDocuments.isNotEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Quick searches",
                        subtitle = "Documents opened in the last 24 hours",
                        actionText = "Clear",
                        onActionClick = viewModel::clearRecentDocuments,
                        modifier = Modifier.padding(
                            start = Dimens.space,
                            end = Dimens.space,
                            top = Dimens.spaceXl,
                            bottom = Dimens.spaceMd
                        )
                    )
                }
                items(state.recentDocuments, key = { "${it.type}:${it.link}" }) { document ->
                    RecentDocumentCard(
                        document = document,
                        onClick = {
                            viewModel.reopenDocument(document)
                            openCustomTab(context, document.link.replace(" ", "%20"))
                        },
                        modifier = Modifier.padding(
                            horizontal = Dimens.space,
                            vertical = Dimens.spaceXs
                        )
                    )
                }
            }

            item {
                HomeSectionHeader(
                    title = "Quick tools",
                    subtitle = "Results and academic resources",
                    modifier = Modifier.padding(
                        start = Dimens.space,
                        end = Dimens.space,
                        top = Dimens.spaceXl,
                        bottom = Dimens.spaceMd
                    )
                )
            }
            items(homeQuickTools.chunked(2)) { rowTools ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.space, vertical = Dimens.spaceXs),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
                ) {
                    rowTools.forEach { tool ->
                        QuickToolCard(
                            tool = tool,
                            onClick = { onTool(tool) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowTools.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            item {
                HomeSectionHeader(
                    title = "Recent searches",
                    subtitle = "Roll numbers opened recently",
                    actionText = if (state.students.isNotEmpty()) "Clear" else null,
                    onActionClick = viewModel::deleteAllStudents,
                    modifier = Modifier.padding(
                        start = Dimens.space,
                        end = Dimens.space,
                        top = Dimens.spaceXl,
                        bottom = Dimens.spaceMd
                    )
                )
            }
            if (state.students.isEmpty()) {
                item {
                    RecentEmpty(
                        title = "No recent roll numbers",
                        subtitle = "Search a hall ticket number above to keep it close at hand."
                    )
                }
            } else {
                items(state.students.take(3), key = { it.rollNumber }) { student ->
                    RecentStudentCard(
                        student = student,
                        onClick = { onOpenStudent(student.rollNumber) },
                        modifier = Modifier.padding(
                            horizontal = Dimens.space,
                            vertical = Dimens.spaceXs
                        )
                    )
                }
            }
        }

        // The hero itself extends behind the status bar. Drawing another gradient
        // here would restart its color stops at the inset and create a visible seam.
        if (heroScrolled) {
            StatusBarScrim(brush = androidx.compose.ui.graphics.SolidColor(homeBackground))
        }
    }
}

@Composable
private fun HomeHero(
    rollValue: String,
    onRollChange: (String) -> Unit,
    onSubmit: () -> Unit,
    error: String?
) {
    val dark = LocalJntuhDarkTheme.current
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
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF232428)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher),
                        contentDescription = "JNTUH Connect logo",
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(Modifier.width(Dimens.spaceMd))
                Text(
                    "JNTUH Connect",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "Student portal",
                    color = Color.White.copy(alpha = 0.58f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "Your academic record,\nmade simple.",
                color = Color.White,
                fontSize = 35.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(Dimens.spaceMd))
            Text(
                "Search results, backlogs, and credits with your hall ticket number.",
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 16.sp,
                lineHeight = 23.sp
            )
            Spacer(Modifier.height(Dimens.spaceXl))
            HeroSearchBar(
                value = rollValue,
                onValueChange = onRollChange,
                onSubmit = onSubmit
            )
            if (error != null) {
                Spacer(Modifier.height(Dimens.spaceSm))
                Text(
                    text = error,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFB4AB)
                )
            }
        }
    }
}

@Composable
private fun HomeSectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 21.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                color = Color(0xFFFF4545),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = Dimens.space).padding(vertical = 5.dp)
                    .clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
private fun RecentEmpty(title: String, subtitle: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space),
        shape = RoundedCornerShape(Dimens.radiusLg),
        color = if (LocalJntuhDarkTheme.current) Color(0xFF1C1C1E) else Color.White
    ) {
        Column(Modifier.padding(Dimens.spaceLg)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(Dimens.spaceXs))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
