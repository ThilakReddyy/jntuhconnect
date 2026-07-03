package com.dhethi.jntuhconnect.presentation.content

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.domain.model.ContentDoc
import com.dhethi.jntuhconnect.domain.model.ContentNode
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.ShimmerList
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.TonalButton
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/** Shared full-screen scaffold for content pages. */
@Composable
private fun ContentScaffold(
    title: String,
    onBack: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(title = title, onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = Dimens.space,
                end = Dimens.space,
                top = Dimens.spaceSm,
                bottom = Dimens.spaceXxl
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd),
            content = content
        )
    }
}

@Composable
private fun IconBadge(icon: ImageVector, tint: Color) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint)
    }
}

// -------------------- Channels --------------------
@Composable
fun ChannelsScreen(navigateBack: () -> Unit) {
    val context = LocalContext.current
    ContentScaffold("Channels", navigateBack) {
        item {
            Text(
                "Join our channels to get instant result alerts and updates.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(ContentData.channels) { channel ->
            AppCard(onClick = { openCustomTab(context, channel.url) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBadge(Icons.Rounded.Campaign, MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(Dimens.space))
                    Column(Modifier.weight(1f)) {
                        Text(channel.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            channel.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusChip(channel.tag, MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

// -------------------- Calendars --------------------
@Composable
fun CalendarsScreen(
    navigateBack: () -> Unit,
    viewModel: CalendarsViewModel = hiltViewModel()
) {
    ContentTreeScreen(
        title = "Academic Calendars",
        state = viewModel.state.value,
        onRetry = viewModel::load,
        navigateBack = navigateBack,
        folderIcon = Icons.Rounded.CalendarMonth,
        accent = MaterialTheme.colorScheme.secondary,
        rootHint = "Select an academic year to browse calendars."
    )
}

// -------------------- Syllabus --------------------
@Composable
fun SyllabusScreen(
    navigateBack: () -> Unit,
    viewModel: SyllabusViewModel = hiltViewModel()
) {
    ContentTreeScreen(
        title = "Syllabus",
        state = viewModel.state.value,
        onRetry = viewModel::load,
        navigateBack = navigateBack,
        folderIcon = Icons.Rounded.MenuBook,
        accent = MaterialTheme.colorScheme.tertiary,
        rootHint = "Select a degree to browse the syllabus."
    )
}

// -------------------- Shared drill-down tree --------------------

/** Walk [root] along the selected [path]; returns the node at that depth, or null. */
private fun walkTree(root: ContentNode, path: List<String>): ContentNode? {
    var node: ContentNode = root
    for (key in path) {
        val branch = node as? ContentNode.Branch ?: return null
        node = branch.children.firstOrNull { it.label == key }?.node ?: return null
    }
    return node
}

/**
 * A generic drill-down browser for the nested calendars / syllabus trees: tap a folder to
 * go deeper, tap a breadcrumb or press back to go up, and tap a document to open its PDF.
 */
@Composable
private fun ContentTreeScreen(
    title: String,
    state: ContentTreeState,
    onRetry: () -> Unit,
    navigateBack: () -> Unit,
    folderIcon: ImageVector,
    accent: Color,
    rootHint: String
) {
    val context = LocalContext.current
    var path by remember { mutableStateOf(emptyList<String>()) }

    // Hardware back pops one drill-down level before leaving the screen.
    BackHandler(enabled = path.isNotEmpty()) { path = path.dropLast(1) }

    val current = state.root?.let { walkTree(it, path) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = title,
                onBack = { if (path.isNotEmpty()) path = path.dropLast(1) else navigateBack() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = Dimens.space,
                end = Dimens.space,
                top = Dimens.spaceSm,
                bottom = Dimens.spaceXxl
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
        ) {
            if (path.isNotEmpty()) {
                item { Breadcrumbs(path = path, onCrumb = { depth -> path = path.take(depth) }) }
            }

            when {
                state.isLoading -> item { ShimmerList(count = 6) }

                state.error.isNotEmpty() -> item {
                    EmptyState(
                        icon = folderIcon,
                        title = "Couldn't load",
                        subtitle = state.error,
                        action = { PrimaryButton("Retry", onClick = onRetry) }
                    )
                }

                current is ContentNode.Branch -> {
                    if (path.isEmpty()) {
                        item {
                            Text(
                                rootHint,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    items(current.children, key = { it.label }) { entry ->
                        FolderRow(
                            label = entry.label,
                            icon = folderIcon,
                            accent = accent,
                            onClick = { path = path + entry.label }
                        )
                    }
                }

                current is ContentNode.Documents -> {
                    if (current.docs.isEmpty()) {
                        item {
                            EmptyState(
                                icon = folderIcon,
                                title = "Nothing here yet",
                                subtitle = "This section has no documents."
                            )
                        }
                    } else {
                        items(current.docs) { doc ->
                            // Some JNTUH PDF links contain literal spaces; encode them so the
                            // custom tab can launch (a no-op for already-encoded URLs).
                            DocumentRow(doc = doc, accent = accent) {
                                openCustomTab(context, doc.link.replace(" ", "%20"))
                            }
                        }
                    }
                }

                else -> item {
                    EmptyState(
                        icon = folderIcon,
                        title = "Nothing here yet",
                        subtitle = "Check back later."
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Breadcrumbs(path: List<String>, onCrumb: (Int) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXs),
        verticalArrangement = Arrangement.Center
    ) {
        Crumb(label = "Home", highlighted = path.isEmpty()) { onCrumb(0) }
        path.forEachIndexed { i, label ->
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Crumb(label = label, highlighted = i == path.lastIndex) { onCrumb(i + 1) }
        }
    }
}

@Composable
private fun Crumb(label: String, highlighted: Boolean, onClick: () -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
        color = if (highlighted) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun FolderRow(
    label: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit
) {
    AppCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon, accent)
            Spacer(Modifier.width(Dimens.space))
            Text(
                label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DocumentRow(doc: ContentDoc, accent: Color, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(Icons.Rounded.Description, accent)
            Spacer(Modifier.width(Dimens.space))
            Text(
                doc.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(Dimens.spaceSm))
            Icon(
                Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------- Careers --------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CareersScreen(navigateBack: () -> Unit) {
    val context = LocalContext.current
    ContentScaffold("Jobs & Careers", navigateBack) {
        items(ContentData.careers) { job ->
            AppCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBadge(Icons.Rounded.WorkOutline, MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(Dimens.space))
                    Column(Modifier.weight(1f)) {
                        Text(job.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${job.company} · ${job.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(Dimens.spaceSm))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
                    job.tags.forEach { StatusChip(it, MaterialTheme.colorScheme.primary) }
                }
                Spacer(Modifier.height(Dimens.spaceSm))
                Text(
                    job.about,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Dimens.spaceMd))
                TonalButton(
                    text = "View & apply",
                    onClick = { openCustomTab(context, job.applyUrl) },
                    icon = Icons.AutoMirrored.Rounded.OpenInNew
                )
            }
        }
    }
}

// -------------------- Help / FAQ --------------------
@Composable
fun HelpScreen(navigateBack: () -> Unit) {
    val context = LocalContext.current
    ContentScaffold("Help Center", navigateBack) {
        item {
            Text(
                "Frequently asked questions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        items(ContentData.faqs) { faq ->
            FaqCard(faq.question, faq.answer)
        }
        item {
            Spacer(Modifier.height(Dimens.spaceSm))
            AppCard(onClick = { openCustomTab(context, ContentData.channels.first().url) }) {
                Text("Still need help?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    "Reach out on our Telegram channel and we'll help you out.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FaqCard(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    AppCard(onClick = { expanded = !expanded }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                question,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        AnimatedVisibility(visible = expanded) {
            Text(
                answer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.spaceSm)
            )
        }
    }
}
