package com.dhethi.jntuhconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
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
import com.dhethi.jntuhconnect.presentation.theme.LocalJntuhDarkTheme

/** Layered search control matching the dark academic-record hero. */
@Composable
fun HeroSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dark = LocalJntuhDarkTheme.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = ShapeLg,
        color = if (dark) Color(0xFF292C31) else Color(0xFF555D66),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (dark) Color(0xFF454950) else Color(0xFF737B84)
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = { onValueChange(normalizeRollNumber(it)) },
                modifier = Modifier
                    .weight(1f)
                    .clip(ShapeMd),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium,
                placeholder = {
                    Text(
                        "Enter roll number",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFBFC2C7)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = Color(0xFFBFC2C7)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (dark) Color(0xFF1E2024) else Color(0xFF3F464E),
                    unfocusedContainerColor = if (dark) Color(0xFF1E2024) else Color(0xFF3F464E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(56.dp)
                    .clip(ShapeMd)
                    .background(Color.White)
                    .clickable(onClick = onSubmit),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
            }
        }
    }
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
        modifier = modifier.height(152.dp),
        shape = ShapeLg,
        color = if (LocalJntuhDarkTheme.current) Color(0xFF1C1C1E) else Color.White,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(Dimens.space),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(ShapeMd)
                    .background(
                        if (LocalJntuhDarkTheme.current) Color(0xFF272729)
                        else Color(0xFFF2F2F3)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(tool.icon, contentDescription = null, tint = tool.accent, modifier = Modifier.size(25.dp))
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
        shape = ShapeLg,
        color = if (LocalJntuhDarkTheme.current) Color(0xFF1C1C1E) else Color.White,
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
                    .background(
                        if (LocalJntuhDarkTheme.current) Color(0xFF29292B)
                        else Color(0xFFF1F1F2)
                    ),
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
        shape = ShapeLg,
        color = if (LocalJntuhDarkTheme.current) Color(0xFF1C1C1E) else Color.White,
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
                    .background(
                        if (LocalJntuhDarkTheme.current) Color(0xFF29292B)
                        else Color(0xFFF1F1F2)
                    ),
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
        shape = ShapeLg,
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
