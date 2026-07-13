package com.dhethi.jntuhconnect.presentation.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.FilterChipRow
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.TonalButton
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val categories = Constants.UPDATES_TABS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatesScreen(
    viewModel: UpdatesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var showFilters by remember { mutableStateOf(false) }
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }

    LaunchedEffect(
        state.currentTab,
        state.degreeCode,
        state.regulation,
        state.year,
        state.titleQuery
    ) {
        if (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) {
            listState.scrollToItem(0)
        }
    }

    if (showFilters) {
        UpdatesFilterSheet(
            state = state,
            onDismiss = { showFilters = false },
            onApply = { dl, dc, reg, yr, title ->
                showFilters = false
                viewModel.applyFilters(dl, dc, reg, yr, title)
            },
            onClear = {
                showFilters = false
                viewModel.clearFilters()
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Updates",
                onBack = navigateBack,
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        BadgedBox(badge = {
                            if (state.activeFilterCount > 0) Badge { Text("${state.activeFilterCount}") }
                        }) {
                            Icon(Icons.Rounded.Tune, contentDescription = "Filter")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(Modifier.height(Dimens.spaceSm))
            FilterChipRow(
                options = categories,
                selected = state.currentTab,
                onSelect = viewModel::switchCategory
            )
            Spacer(Modifier.height(Dimens.spaceSm))

            if (state.error.isNotEmpty() && state.updates.isEmpty()) {
                EmptyState(
                    icon = Icons.Rounded.NotificationsNone,
                    title = "Couldn't load updates",
                    subtitle = state.error,
                    action = { PrimaryButton("Retry", onClick = viewModel::retry) }
                )
                return@Column
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = Dimens.space, end = Dimens.space, bottom = Dimens.spaceXxl),
                verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
            ) {
                items(state.updates) { update ->
                    UpdateCard(
                        update = update,
                        isNew = update.releaseDate == today,
                        onClick = { openCustomTab(context, update.link) }
                    )
                }
                if (state.error.isNotEmpty() && state.updates.isNotEmpty()) {
                    item {
                        AppCard {
                            Text(
                                "More updates could not be loaded",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(Dimens.spaceXs))
                            Text(
                                state.error,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(Dimens.spaceMd))
                            TonalButton("Try again", onClick = viewModel::retry)
                        }
                    }
                }
                if (state.isLoading) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(Dimens.space),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }
                }
                if (!state.isLoading && state.updates.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Rounded.NotificationsNone,
                            title = "No updates found",
                            subtitle = "Try changing the category or clearing filters."
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisible != null && lastVisible >= total - 3) {
                    viewModel.loadNextPage()
                }
            }
    }
}

@Composable
private fun UpdateCard(
    update: LatestNotification,
    isNew: Boolean,
    onClick: () -> Unit
) {
    AppCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isNew) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary
                    )
            )
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(Modifier.weight(1f)) {
                Text(
                    update.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(Dimens.spaceSm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (update.date.isNotBlank()) {
                        Text(
                            update.date.replace("-", " "),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    if (isNew) {
                        StatusChip("New", MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.width(Dimens.spaceXs))
                    }
                    StatusChip(update.category.ifBlank { "Update" }, MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun UpdatesFilterSheet(
    state: UpdatesState,
    onDismiss: () -> Unit,
    onApply: (degreeLabel: String, degreeCode: String, regulation: String, year: String, title: String) -> Unit,
    onClear: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var degreeLabel by remember { mutableStateOf(state.degreeLabel) }
    var regulation by remember { mutableStateOf(state.regulation) }
    var year by remember { mutableStateOf(state.year) }
    var title by remember { mutableStateOf(state.titleQuery) }

    val degreeCodes = mapOf(
        "B.Tech" to "btech", "B.Pharmacy" to "bpharmacy", "M.Tech" to "mtech",
        "M.Pharmacy" to "mpharmacy", "MBA" to "mba", "MCA" to "mca"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = Dimens.spaceLg)
                .padding(bottom = Dimens.spaceLg)
        ) {
            Text("Filter updates", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(Dimens.space))

            FilterGroup("Degree") {
                ContentData.degrees.forEach { d ->
                    ChoiceChip(d, degreeLabel == d) { degreeLabel = if (degreeLabel == d) "" else d }
                }
            }
            FilterGroup("Regulation") {
                ContentData.regulations.forEach { (code, _) ->
                    ChoiceChip(code, regulation == code) { regulation = if (regulation == code) "" else code }
                }
            }
            FilterGroup("Year") {
                ContentData.examYears.forEach { y ->
                    ChoiceChip(y, year == y) { year = if (year == y) "" else y }
                }
            }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Search title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(Dimens.spaceLg))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)) {
                TonalButton(text = "Clear", onClick = onClear, modifier = Modifier.weight(1f))
                PrimaryButton(
                    text = "Apply",
                    onClick = { onApply(degreeLabel, degreeCodes[degreeLabel] ?: "", regulation, year, title) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterGroup(title: String, content: @Composable () -> Unit) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(Dimens.spaceSm))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceSm),
    ) { content() }
    Spacer(Modifier.height(Dimens.space))
}

@Composable
private fun ChoiceChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
