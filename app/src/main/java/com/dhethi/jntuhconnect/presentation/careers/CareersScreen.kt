package com.dhethi.jntuhconnect.presentation.careers

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.LaptopChromebook
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.data.remote.dto.JobDto
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.accentAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareersScreen(
    navigateBack: () -> Unit,
    viewModel: CareersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val listState = rememberLazyListState()
    var searchText by rememberSaveable { mutableStateOf(state.keyword) }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var selectedJob by remember { mutableStateOf<JobDto?>(null) }

    if (showFilters) {
        CareersFilterSheet(
            state = state,
            onDismiss = { showFilters = false },
            onApply = { type, companyType, remote ->
                showFilters = false
                viewModel.applyFilters(type, companyType, remote)
            },
            onClear = {
                showFilters = false
                viewModel.clearFilters()
            }
        )
    }

    selectedJob?.let { job ->
        JobDetailsSheet(job = job, onDismiss = { selectedJob = null })
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Jobs & Careers",
                onBack = navigateBack,
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh jobs")
                    }
                    IconButton(onClick = { showFilters = true }) {
                        BadgedBox(badge = {
                            if (state.activeFilterCount > 0) {
                                Badge { Text(state.activeFilterCount.toString()) }
                            }
                        }) {
                            Icon(Icons.Rounded.FilterAlt, contentDescription = "Filter jobs")
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
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
                singleLine = true,
                label = { Text("Search opportunities") },
                placeholder = { Text("Role, company, or skill") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.search(searchText) }) {
                        Icon(Icons.Rounded.Search, contentDescription = "Search jobs")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.search(searchText) })
            )

            QuickFilters(
                state = state,
                onApply = viewModel::applyFilters,
                onMore = { showFilters = true }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.WorkOutline,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconSm),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(Dimens.spaceSm))
                Text(
                    if (state.isLoading) "Checking current openings…"
                    else "${state.jobs.size} opportunities loaded",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Updated daily",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            when {
                state.error.isNotBlank() && state.jobs.isEmpty() -> EmptyState(
                    icon = Icons.Rounded.WorkOutline,
                    title = "Couldn't load opportunities",
                    subtitle = state.error,
                    action = { PrimaryButton("Try again", onClick = viewModel::retry) }
                )

                state.isLoading && state.jobs.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                state.jobs.isEmpty() -> EmptyState(
                    icon = Icons.Rounded.Search,
                    title = "No matching opportunities",
                    subtitle = "Try a broader search or clear your filters.",
                    action = {
                        PrimaryButton(
                            "Clear filters",
                            onClick = {
                                searchText = ""
                                viewModel.search("")
                                viewModel.clearFilters()
                            }
                        )
                    }
                )

                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Dimens.space,
                        end = Dimens.space,
                        top = Dimens.spaceSm,
                        bottom = Dimens.spaceXxl
                    ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
                ) {
                    items(state.jobs, key = { it.id }) { job ->
                        JobCard(job = job, onClick = { selectedJob = job })
                    }
                    if (state.error.isNotBlank()) {
                        item {
                            AppCard {
                                Text("More jobs couldn't be loaded", fontWeight = FontWeight.SemiBold)
                                Text(
                                    state.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                TextButton(onClick = viewModel::retry) { Text("Try again") }
                            }
                        }
                    }
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                Modifier.fillMaxWidth().padding(Dimens.space),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator(modifier = Modifier.size(28.dp)) }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(listState, state.hasMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val total = listState.layoutInfo.totalItemsCount
                if (state.hasMore && lastVisible != null && lastVisible >= total - 3) {
                    viewModel.loadNextPage()
                }
            }
    }
}

@Composable
private fun QuickFilters(
    state: CareersState,
    onApply: (String, String, Boolean?) -> Unit,
    onMore: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.space),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)
    ) {
        item {
            FilterChip(
                selected = state.type == "INTERN",
                onClick = {
                    onApply(if (state.type == "INTERN") "" else "INTERN", state.companyType, state.remote)
                },
                label = { Text("Internships") }
            )
        }
        item {
            FilterChip(
                selected = state.companyType == "PRODUCT",
                onClick = {
                    onApply(state.type, if (state.companyType == "PRODUCT") "" else "PRODUCT", state.remote)
                },
                label = { Text("Product companies") }
            )
        }
        item {
            FilterChip(
                selected = state.remote == true,
                onClick = { onApply(state.type, state.companyType, if (state.remote == true) null else true) },
                label = { Text("Remote") }
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = onMore,
                label = { Text("All filters") },
                leadingIcon = {
                    Icon(Icons.Rounded.FilterAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun JobCard(job: JobDto, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXs)
        ) {
            StatusChip(formatJobType(job.type), MaterialTheme.colorScheme.primary)
            if (job.isProductBased) StatusChip("Product company", accentAmber)
        }
        Spacer(Modifier.height(Dimens.spaceMd))
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(
                    job.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(Dimens.spaceXs))
                Text(
                    job.displayCompany,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(Dimens.spaceMd))
        JobInfoLine(Icons.Rounded.LocationOn, job.displayLocation)
        Spacer(Modifier.height(Dimens.spaceSm))
        JobInfoLine(Icons.Rounded.School, experienceLabel(job))
        Spacer(Modifier.height(Dimens.spaceSm))
        JobInfoLine(
            Icons.Rounded.LaptopChromebook,
            if (job.isRemote) "Remote eligible" else "Office / hybrid"
        )
        Spacer(Modifier.height(Dimens.spaceMd))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(Dimens.spaceSm))
        Text(
            "${formatSource(job.source)} source · Verified daily",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun JobInfoLine(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(17.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(Dimens.spaceSm))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun JobDetailsSheet(job: JobDto, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val maxSheetHeight = LocalConfiguration.current.screenHeightDp.dp * 0.75f
    // ModalBottomSheet owns the bottom anchor and contributes the drag-handle area.
    // Constrain its content so the complete surface occupies the lower 75%.
    val sheetContentHeight = maxSheetHeight - 72.dp
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetContentHeight),
            contentPadding = PaddingValues(
                start = Dimens.space,
                end = Dimens.space,
                bottom = Dimens.spaceXxl
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
        ) {
            item {
                Text(
                    "OPPORTUNITY BRIEF · ${formatSource(job.source).uppercase()} SOURCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Dimens.spaceMd))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
                    StatusChip(formatJobType(job.type), MaterialTheme.colorScheme.primary)
                    StatusChip(formatCompanyType(job.companyType), MaterialTheme.colorScheme.tertiary)
                }
                Spacer(Modifier.height(Dimens.spaceMd))
                Text(job.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    job.displayCompany,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                AppCard {
                    JobInfoLine(Icons.Rounded.LocationOn, job.displayLocation)
                    Spacer(Modifier.height(Dimens.spaceMd))
                    JobInfoLine(Icons.Rounded.School, experienceLabel(job))
                    Spacer(Modifier.height(Dimens.spaceMd))
                    JobInfoLine(
                        Icons.Rounded.LaptopChromebook,
                        if (job.isRemote) "Remote eligible" else "Office / hybrid"
                    )
                    job.salary?.takeIf { it.isNotBlank() }?.let { salary ->
                        Spacer(Modifier.height(Dimens.spaceMd))
                        JobInfoLine(Icons.Rounded.Business, salary)
                    }
                }
            }
            if (job.tags.any { it.isNotBlank() }) {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm),
                        verticalArrangement = Arrangement.spacedBy(Dimens.spaceSm)
                    ) {
                        job.tags.filter { it.isNotBlank() }.take(8).forEach { tag ->
                            StatusChip(tag, MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
            item {
                Text("Role overview", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(Dimens.spaceSm))
                Text(
                    job.description.ifBlank {
                        "Open the official application page for the complete role description and eligibility criteria."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryButton(
                        text = "Apply on company site",
                        onClick = { job.applicationUrl?.let { openCustomTab(context, it) } },
                        modifier = Modifier.weight(1f),
                        enabled = !job.applicationUrl.isNullOrBlank(),
                        icon = Icons.AutoMirrored.Rounded.OpenInNew
                    )
                    Spacer(Modifier.width(Dimens.spaceSm))
                    IconButton(
                        onClick = {
                            val url = job.applicationUrl ?: return@IconButton
                            context.startActivity(
                                Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "${job.title} at ${job.displayCompany}")
                                        putExtra(Intent.EXTRA_TEXT, url)
                                    },
                                    "Share opportunity"
                                )
                            )
                        },
                        enabled = !job.applicationUrl.isNullOrBlank()
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = "Share opportunity")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun CareersFilterSheet(
    state: CareersState,
    onDismiss: () -> Unit,
    onApply: (String, String, Boolean?) -> Unit,
    onClear: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var type by rememberSaveable { mutableStateOf(state.type) }
    var companyType by rememberSaveable { mutableStateOf(state.companyType) }
    var remote by rememberSaveable { mutableStateOf(state.remote) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.space, vertical = Dimens.spaceSm)
        ) {
            Text("Filter opportunities", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(Dimens.spaceLg))
            FilterGroup(
                title = "Opportunity",
                options = listOf("" to "All", "INTERN" to "Internships", "FULL_TIME" to "Full time", "PART_TIME" to "Part time"),
                selected = type,
                onSelect = { type = it }
            )
            Spacer(Modifier.height(Dimens.spaceLg))
            FilterGroup(
                title = "Company",
                options = listOf("" to "All", "PRODUCT" to "Product", "SERVICE" to "Service", "OTHER" to "Other"),
                selected = companyType,
                onSelect = { companyType = it }
            )
            Spacer(Modifier.height(Dimens.spaceLg))
            Text("Work mode", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
                listOf(null to "Any", true to "Remote", false to "Office / hybrid").forEach { (value, label) ->
                    FilterChip(
                        selected = remote == value,
                        onClick = { remote = value },
                        label = { Text(label) }
                    )
                }
            }
            Spacer(Modifier.height(Dimens.spaceXl))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onClear) { Text("Clear") }
                Spacer(Modifier.weight(1f))
                PrimaryButton("Apply filters", onClick = { onApply(type, companyType, remote) })
            }
            Spacer(Modifier.height(Dimens.space))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterGroup(
    title: String,
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
        options.forEach { (value, label) ->
            val isSelected = selected == value
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(value) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

private fun formatJobType(type: String): String = when (type.uppercase()) {
    "INTERN" -> "Internship"
    "PART_TIME" -> "Part time"
    else -> "Full time"
}

private fun formatCompanyType(type: String): String = when (type.uppercase()) {
    "PRODUCT" -> "Product company"
    "SERVICE" -> "Service company"
    else -> "Other company"
}

private fun formatSource(source: String): String = source
    .substringBefore(':')
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    .ifBlank { "Official" }

private fun experienceLabel(job: JobDto): String {
    if (job.type.equals("INTERN", ignoreCase = true)) return "Students & graduates"
    val minimum = job.experienceMin
    val maximum = job.experienceMax
    return when {
        minimum != null && maximum != null -> "$minimum–$maximum years"
        minimum != null -> "$minimum+ years"
        else -> "Early career"
    }
}
