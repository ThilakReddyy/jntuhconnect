package com.dhethi.jntuhconnect.presentation.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.dhethi.jntuhconnect.presentation.Screen
import com.dhethi.jntuhconnect.presentation.components.RollInputSheet
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.isValidRollNumber
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.explore.ToolAction
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.explore.homeQuickTools
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.LocalJntuhDarkTheme
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg
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
    val largeText = LocalDensity.current.fontScale > 1.2f
    val keyboard = LocalSoftwareKeyboardController.current
    var roll by rememberSaveable { mutableStateOf("") }
    var searchAttempted by rememberSaveable { mutableStateOf(false) }
    var showClearSearchesConfirmation by rememberSaveable { mutableStateOf(false) }
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

    if (showClearSearchesConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearSearchesConfirmation = false },
            title = { Text("Clear recent searches?") },
            text = {
                Text(
                    "This removes every saved roll number from this device and turns off " +
                        "result alerts for them."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearSearchesConfirmation = false
                        viewModel.deleteAllStudents()
                    }
                ) {
                    Text("Clear all", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearSearchesConfirmation = false }) {
                    Text("Cancel")
                }
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

    val listState = rememberLazyListState()
    val heroScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }
    val darkTheme = LocalJntuhDarkTheme.current
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme && heroScrolled
        }
        DisposableEffect(window, view, darkTheme) {
            onDispose {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    !darkTheme
            }
        }
    }
    val homeBackground = MaterialTheme.colorScheme.background
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

            if (state.students.isNotEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Continue where you left off",
                        subtitle = "Your latest result searches",
                        actionText = "Clear all",
                        onActionClick = { showClearSearchesConfirmation = true },
                        modifier = Modifier.padding(
                            start = Dimens.space,
                            end = Dimens.space,
                            top = Dimens.spaceXl,
                            bottom = Dimens.spaceMd
                        )
                    )
                }
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

            item {
                HomeSectionHeader(
                    title = "Quick tools",
                    subtitle = "Your most-used academic tools",
                    modifier = Modifier.padding(
                        start = Dimens.space,
                        end = Dimens.space,
                        top = Dimens.spaceXl,
                        bottom = Dimens.spaceMd
                    )
                )
            }
            item {
                QuickToolsGrid(
                    tools = homeQuickTools,
                    largeText = largeText,
                    onTool = ::onTool
                )
            }

            if (state.students.isEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Recent searches",
                        subtitle = "Roll numbers opened recently",
                        modifier = Modifier.padding(
                            start = Dimens.space,
                            end = Dimens.space,
                            top = Dimens.spaceXl,
                            bottom = Dimens.spaceMd
                        )
                    )
                }
                item {
                    RecentEmpty(
                        title = "No recent roll numbers",
                        subtitle = "Search a hall ticket number above to keep it close at hand."
                    )
                }
            }

            if (state.latestUpdates.isNotEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Latest updates",
                        subtitle = "New from JNTUH",
                        actionText = "View all",
                        onActionClick = { onOpenRoute(Screen.Updates.route) },
                        modifier = Modifier.padding(
                            start = Dimens.space,
                            end = Dimens.space,
                            top = Dimens.spaceXl,
                            bottom = Dimens.spaceMd
                        )
                    )
                }
                items(
                    items = state.latestUpdates,
                    key = { update -> "${update.link}:${update.releaseDate}" }
                ) { update ->
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
                        title = "Recently opened",
                        subtitle = "Documents from the last 24 hours",
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
                DhethiFooterCard(
                    onClick = { onOpenRoute(Screen.Profile.route) },
                    modifier = Modifier.padding(
                        horizontal = Dimens.space,
                        vertical = Dimens.spaceLg
                    )
                )
            }

        }

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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = Dimens.radiusXl,
                    bottomEnd = Dimens.radiusXl
                )
            )
            .background(brandGradient(dark))
    ) {
        val compact = maxWidth < 360.dp
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 52.dp, y = (-54).dp)
                .size(176.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.035f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-42).dp, y = 58.dp)
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.025f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = if (compact) Dimens.space else Dimens.spaceLg)
                .padding(top = Dimens.space, bottom = Dimens.spaceXl)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(if (compact) 40.dp else 44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.14f)
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
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Default
                    ),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(if (compact) Dimens.spaceXl else Dimens.spaceXxl))
            Text(
                "Your academic record,\nright at hand",
                color = Color.White,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = if (compact) 29.sp else 33.sp,
                    lineHeight = if (compact) 35.sp else 40.sp
                ),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(Dimens.spaceSm))
            Text(
                "Results, backlogs and credit progress in one place.",
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(Dimens.spaceLg))
            HeroSearchBar(
                value = rollValue,
                onValueChange = onRollChange,
                onSubmit = onSubmit,
                onBrand = true
            )
            if (error != null) {
                Spacer(Modifier.height(Dimens.spaceSm))
                Text(
                    text = error,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFDAD6)
                )
            }
        }
    }
}

@Composable
private fun QuickToolsGrid(
    tools: List<ToolItem>,
    largeText: Boolean,
    onTool: (ToolItem) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.space),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        if (largeText) {
            tools.forEach { tool ->
                QuickToolCard(
                    tool = tool,
                    onClick = { onTool(tool) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            tools.chunked(2).forEach { rowTools ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) { Text(actionText) }
        }
    }
}

@Composable
private fun RecentEmpty(title: String, subtitle: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.space),
        shape = ShapeLg,
        color = MaterialTheme.colorScheme.surfaceContainer
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

@Composable
private fun DhethiFooterCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = ShapeLg,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Dimens.space,
                vertical = Dimens.spaceMd
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "d.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(Modifier.width(Dimens.space))
            Column(Modifier.weight(1f)) {
                Text(
                    "Built by Dhethi",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Independent studio behind JNTUH Connect. Have an idea, or need something built?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(Dimens.spaceSm))
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
