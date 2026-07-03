package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import com.dhethi.jntuhconnect.domain.model.Semester
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.gradeColor
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/**
 * Expandable per-semester card used on the Academic and Backlog tabs. Shows the
 * semester number, an SGPA chip and credits summary, and the subject rows.
 */
@Composable
fun SemesterCard(
    semester: Semester,
    modifier: Modifier = Modifier,
    showSgpa: Boolean = true,
    initiallyExpanded: Boolean = false,
    dashForZeroMarks: Boolean = true
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val sgpa = semester.semesterSGPA
    val sgpaColor = when {
        semester.failed -> MaterialTheme.colorScheme.error
        (sgpa.toFloatOrNull() ?: 0f) >= 8f -> gradeColor("O")
        else -> gradeColor("B")
    }

    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Semester ${semester.semester}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${creditText(semester.semesterCredits)} credits · ${semester.subjects.size} subjects",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (showSgpa) {
                StatusChip(text = "SGPA $sgpa", color = sgpaColor)
                Spacer(Modifier.width(Dimens.spaceSm))
            } else if (semester.failed) {
                StatusChip(
                    text = "${semester.backlogs.toInt()} backlog(s)",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(Dimens.spaceSm))
            }
            Icon(
                Icons.Rounded.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.rotate(if (expanded) 180f else 0f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(Dimens.spaceSm))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                semester.subjects.forEachIndexed { index, subject ->
                    if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    SubjectRow(subject = subject, dashForZeroMarks = dashForZeroMarks)
                }
            }
        }
    }
}
