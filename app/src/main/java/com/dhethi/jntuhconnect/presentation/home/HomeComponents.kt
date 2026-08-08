package com.dhethi.jntuhconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd

/** Layered search control matching the dark academic-record hero. */
@Composable
fun HeroSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    onBrand: Boolean = false
) {
    val container = if (onBrand) Color.White.copy(alpha = 0.13f)
    else MaterialTheme.colorScheme.surfaceContainerHigh
    val content = if (onBrand) Color.White else MaterialTheme.colorScheme.onSurface
    val muted = if (onBrand) Color.White.copy(alpha = 0.72f)
    else MaterialTheme.colorScheme.onSurfaceVariant
    val actionContainer = if (onBrand) Color.White else MaterialTheme.colorScheme.primary
    val actionContent = if (onBrand) Color(0xFF202329) else MaterialTheme.colorScheme.onPrimary

    TextField(
        value = value,
        onValueChange = { onValueChange(normalizeRollNumber(it)) },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp),
        singleLine = true,
        shape = ShapeMd,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text("Hall ticket number", color = muted) },
        placeholder = { Text("e.g. 23XX1A05XX") },
        leadingIcon = {
            Icon(
                Icons.Rounded.Search,
                contentDescription = null,
                tint = muted
            )
        },
        trailingIcon = {
            FilledIconButton(
                onClick = onSubmit,
                modifier = Modifier
                    .padding(end = Dimens.spaceSm)
                    .size(Dimens.touchTarget),
                shape = ShapeMd,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = actionContainer,
                    contentColor = actionContent
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = container,
            unfocusedContainerColor = container,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = content,
            unfocusedTextColor = content,
            focusedPlaceholderColor = muted,
            unfocusedPlaceholderColor = muted,
            focusedLabelColor = muted,
            unfocusedLabelColor = muted,
            cursorColor = content
        )
    )
}

/** Quick-tool tile used in the horizontally scrollable Home row. */
@Composable
fun QuickToolCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 132.dp),
        shape = ShapeLg,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(Dimens.space),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(ShapeMd)
                        .background(tool.accent.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tool.icon,
                        contentDescription = null,
                        tint = tool.accent,
                        modifier = Modifier.size(23.dp)
                    )
                }
                Icon(
                    Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(Dimens.iconSm)
                )
            }
            Column {
                Text(
                    tool.homeTitle(),
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Default),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    tool.homeSubtitle(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
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
        shape = ShapeLg,
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
    val largeText = LocalDensity.current.fontScale > 1.2f
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name.initials(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    student.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Default),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = if (largeText) 2 else 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    if (largeText) student.rollNumber else "${student.rollNumber}  ·  ${student.branch}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (largeText && student.branch.isNotBlank()) {
                    Text(
                        student.branch,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    student.summaryLine(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (largeText) 2 else 1,
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

private fun StudentDetailsEntity.summaryLine(): String {
    val semesters = "$semester ${if (semester == 1) "semester" else "semesters"}"
    return if (backlogs > 0) {
        "$semesters  ·  $backlogs ${if (backlogs == 1) "backlog" else "backlogs"}"
    } else {
        "CGPA $cgpa  ·  $semesters  ·  No backlogs"
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
