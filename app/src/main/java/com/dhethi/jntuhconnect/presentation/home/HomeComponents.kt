package com.dhethi.jntuhconnect.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import com.dhethi.jntuhconnect.presentation.components.GradeDot
import com.dhethi.jntuhconnect.presentation.components.normalizeRollNumber
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd

/** Material hall-ticket input used by the primary result lookup. */
@Composable
fun HeroSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(normalizeRollNumber(it)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = ShapeMd,
        label = { Text("Hall ticket number") },
        placeholder = { Text("e.g. 18E51A0479") },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        supportingText = error?.let { message -> { Text(message) } },
        isError = error != null,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onSubmit() })
    )
}

/** Large quick-tool tile used in the two-column Home grid. */
@Composable
fun QuickToolCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(124.dp),
        shape = ShapeMd,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = Dimens.elevationSm
    ) {
        Column(
            modifier = Modifier.padding(Dimens.space),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(ShapeMd)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(tool.icon, contentDescription = null, tint = tool.accent, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(
                    tool.homeTitle(),
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Default),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    tool.homeSubtitle(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun ToolItem.homeTitle() = when (title) {
    "Credits Checker" -> "Credits"
    "Result Contrast" -> "Compare results"
    else -> title
}

private fun ToolItem.homeSubtitle() = when (title) {
    "Credits Checker" -> "Track progress"
    "Class Result" -> "View class rankings"
    "Result Contrast" -> "Compare students"
    "Grace Marks" -> "Check eligibility"
    else -> subtitle
}

@Composable
fun RecentDocumentCard(
    document: RecentDocument,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = ShapeMd,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(Dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(ShapeMd)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (document.type == RecentDocument.CALENDAR) {
                        Icons.Rounded.CalendarMonth
                    } else {
                        Icons.Rounded.MenuBook
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(Modifier.weight(1f)) {
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Default),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (document.type == RecentDocument.CALENDAR) "Academic calendar" else "Syllabus",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Rich recent-search student card. */
@Composable
fun RecentStudentCard(
    student: StudentDetailsEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = ShapeMd,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(Dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(ShapeMd)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    student.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Default),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${student.rollNumber}  ·  ${student.branch}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Compact notification preview used in the Home "Latest updates" strip. */
@Composable
fun UpdatePreviewCard(
    update: LatestNotification,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = ShapeMd,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = Dimens.elevationSm
    ) {
        Row(
            modifier = Modifier.padding(Dimens.space),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GradeDot(
                grade = "O",
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(8.dp)
            )
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    update.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (update.date.isNotBlank()) {
                    Text(
                        update.date.replace("-", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun String.initials(): String =
    split(" ").filter { it.isNotBlank() }.takeLast(2)
        .joinToString("") { it.take(1).uppercase() }
        .ifBlank { "?" }
