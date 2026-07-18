package com.dhethi.jntuhconnect.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.presentation.components.RollInputSheet
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.isValidRollNumber
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.explore.ToolAction
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.explore.homeQuickTools
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd

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

    val listState = rememberLazyListState()
    val headerScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = Dimens.spaceXxl)
        ) {
            item {
                HomeHeader(
                    rollValue = roll,
                    onRollChange = { roll = it },
                    onSubmit = ::submitSearch,
                    error = rollError
                )
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

            if (state.latestUpdates.isNotEmpty()) {
                item {
                    HomeSectionHeader(
                        title = "Latest updates",
                        subtitle = "New from JNTUH",
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

        if (headerScrolled) {
            StatusBarScrim(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.background))
        }
    }
}

@Composable
private fun HomeHeader(
    rollValue: String,
    onRollChange: (String) -> Unit,
    onSubmit: () -> Unit,
    error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = Dimens.space)
            .padding(top = Dimens.spaceMd)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceXs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = ShapeMd,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            Column {
                Text(
                    "JNTUH Connect",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Student academic companion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(Dimens.spaceXl))
        Surface(
            shape = ShapeLg,
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = Dimens.elevationSm
        ) {
            Column(Modifier.padding(Dimens.spaceLg)) {
                Text(
                    "Find student results",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Dimens.spaceXs))
                Text(
                    "Enter a hall ticket number to view results, backlogs, and credits.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Dimens.spaceLg))
                HeroSearchBar(
                    value = rollValue,
                    onValueChange = onRollChange,
                    onSubmit = onSubmit,
                    error = error
                )
                Spacer(Modifier.height(Dimens.spaceMd))
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = ShapeMd
                ) {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                    Spacer(Modifier.width(Dimens.spaceSm))
                    Text("View results")
                }
            }
        }
        Spacer(Modifier.height(Dimens.spaceXs))
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
