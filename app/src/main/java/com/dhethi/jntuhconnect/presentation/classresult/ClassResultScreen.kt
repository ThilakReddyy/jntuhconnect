package com.dhethi.jntuhconnect.presentation.classresult

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.domain.model.ClassBacklogStudent
import com.dhethi.jntuhconnect.domain.model.ClassStudent
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.RollNumberField
import com.dhethi.jntuhconnect.presentation.components.SegmentedTabs
import com.dhethi.jntuhconnect.presentation.components.ShimmerList
import com.dhethi.jntuhconnect.presentation.components.StatTile
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.gradeColor
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun ClassResultScreen(
    viewModel: ClassResultViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val rankedAcademic = state.academic.sortedByDescending { it.cgpaValue() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Class Result", onBack = navigateBack) }
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
            item {
                AppCard {
                    Text(
                        "View an entire class",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Enter any roll number from the section.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Dimens.space))
                    RollNumberField(
                        value = state.roll,
                        onValueChange = viewModel::updateRoll,
                        showSearchAction = false
                    )
                    Spacer(Modifier.height(Dimens.spaceMd))
                    SegmentedTabs(
                        options = listOf(CLASS_TAB_ACADEMIC, CLASS_TAB_BACKLOGS),
                        selected = state.type,
                        onSelect = viewModel::setType
                    )
                    Spacer(Modifier.height(Dimens.space))
                    PrimaryButton(
                        text = "Load class",
                        onClick = viewModel::load,
                        loading = state.isLoading,
                        icon = Icons.Rounded.Groups,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (state.error.isNotEmpty()) {
                item {
                    AppCard(containerColor = MaterialTheme.colorScheme.errorContainer) {
                        Text(
                            state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            if (state.isLoading) {
                item { ShimmerList(count = 4) }
            }

            if (!state.isLoading && state.loaded && state.error.isEmpty()) {
                if (state.type == CLASS_TAB_ACADEMIC) {
                    if (rankedAcademic.isEmpty()) {
                        item { EmptyClass() }
                    } else {
                        item { AcademicSummary(rankedAcademic) }
                        itemsIndexed(rankedAcademic, key = { _, s -> s.details.rollNumber }) { index, student ->
                            AcademicStudentCard(rank = index + 1, student = student)
                        }
                    }
                } else {
                    val withBacklogs = state.backlogs
                        .filter { (it.backlogResult?.totalBacklogs ?: 0) > 0 }
                        .sortedByDescending { it.backlogResult?.totalBacklogs ?: 0 }
                    if (withBacklogs.isEmpty()) {
                        item {
                            EmptyState(
                                icon = Icons.Rounded.Groups,
                                title = "No backlogs in this class 🎉",
                                subtitle = "Every student has cleared their subjects.",
                                tint = gradeColor("O")
                            )
                        }
                    } else {
                        items(withBacklogs, key = { it.details.rollNumber }) { student ->
                            BacklogStudentCard(student)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AcademicSummary(students: List<ClassStudent>) {
    val topper = students.firstOrNull { it.result != null && (it.result.backlogs) == 0 }
    val clean = students.mapNotNull { it.result }.filter { it.backlogs == 0 }
        .mapNotNull { it.cgpa.toFloatOrNull() }
    val avg = if (clean.isNotEmpty()) "%.2f".format(clean.average()) else "—"

    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatTile(value = students.size.toString(), label = "Students", modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Dimens.spaceSm))
            StatTile(value = avg, label = "Avg CGPA", modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Dimens.spaceSm))
            StatTile(
                value = topper?.result?.cgpa ?: "—",
                label = "Top CGPA",
                modifier = Modifier.weight(1f)
            )
        }
        if (topper != null) {
            Spacer(Modifier.height(Dimens.spaceMd))
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.width(Dimens.spaceSm))
                Text(
                    "Topper: ${topper.details.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AcademicStudentCard(rank: Int, student: ClassStudent) {
    val backlogs = student.result?.backlogs ?: 0
    val cgpa = if (backlogs > 0) "—" else (student.result?.cgpa ?: "—")
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RankBadge(rank)
            Spacer(Modifier.width(Dimens.space))
            Column(Modifier.weight(1f)) {
                Text(
                    student.details.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    student.details.rollNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusChip(
                    "CGPA $cgpa",
                    if (backlogs > 0) MaterialTheme.colorScheme.error else gradeColor("A")
                )
                if (backlogs > 0) {
                    Spacer(Modifier.height(Dimens.spaceXxs))
                    Text(
                        "$backlogs backlog(s)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun BacklogStudentCard(student: ClassBacklogStudent) {
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    student.details.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    student.details.rollNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusChip(
                "${student.backlogResult?.totalBacklogs ?: 0} backlog(s)",
                MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun RankBadge(rank: Int) {
    val color = when (rank) {
        1 -> Color(0xFFF59E0B)
        2 -> Color(0xFF94A3B8)
        3 -> Color(0xFFB45309)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (rank <= 3) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$rank",
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyClass() {
    EmptyState(
        icon = Icons.Rounded.Groups,
        title = "No results yet",
        subtitle = "This class hasn't synced yet. Try again in a little while."
    )
}

private fun ClassStudent.cgpaValue(): Float {
    val r = result ?: return -1f
    if (r.backlogs > 0) return -1f
    return r.cgpa.toFloatOrNull() ?: -1f
}
