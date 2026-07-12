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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.presentation.components.GradeDot
import com.dhethi.jntuhconnect.presentation.components.ROLL_NUMBER_LENGTH
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.gradeColor
import com.dhethi.jntuhconnect.presentation.explore.ToolItem
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.Shape
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd
import com.dhethi.jntuhconnect.presentation.theme.brandGradient
import androidx.compose.foundation.isSystemInDarkTheme

/** White search bar that sits on the gradient hero. */
@Composable
fun HeroSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = Shape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = Dimens.elevation
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it.uppercase().take(ROLL_NUMBER_LENGTH)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium,
                placeholder = {
                    Text(
                        "Enter roll number",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            val dark = isSystemInDarkTheme()
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .size(48.dp)
                    .clip(ShapeMd)
                    .background(brandGradient(dark))
                    .clickable(onClick = onSubmit),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}

/** Compact quick-tool card for the Home "Quick tools" row. */
@Composable
fun QuickToolCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.width(150.dp),
        shape = ShapeLg,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = Dimens.elevationSm
    ) {
        Column(modifier = Modifier.padding(Dimens.space)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(ShapeMd)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(tool.icon, contentDescription = null, tint = tool.accent, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(Dimens.spaceSm))
            Text(
                tool.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                tool.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
    val dark = isSystemInDarkTheme()
    val cgpa = if (student.backlogs > 0) "—" else student.cgpa
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
            Box(
                modifier = Modifier
                    .size(Dimens.avatar)
                    .clip(CircleShape)
                    .background(brandGradient(dark)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name.initials(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(Dimens.space))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    student.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    student.rollNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    student.branch,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(Dimens.spaceSm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(
                        text = "CGPA $cgpa",
                        color = if (student.backlogs > 0) MaterialTheme.colorScheme.error
                        else gradeColor("A")
                    )
                    Spacer(Modifier.width(Dimens.spaceSm))
                    StatusChip(
                        text = if (student.backlogs > 0) "${student.backlogs} backlog(s)" else "No backlogs",
                        color = if (student.backlogs > 0) MaterialTheme.colorScheme.error else gradeColor("O")
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
