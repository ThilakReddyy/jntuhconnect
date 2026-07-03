package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.gradeColor
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun BacklogsResults(studentResult: BacklogResult?) {
    val result = studentResult ?: return

    if (result.totalBacklogs <= 0) {
        EmptyState(
            icon = Icons.Rounded.CheckCircle,
            title = "No backlogs 🎉",
            subtitle = "Every subject has been cleared. Keep it up!",
            tint = gradeColor("O")
        )
        return
    }

    Column(
        modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        AppCard(containerColor = MaterialTheme.colorScheme.errorContainer) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(Modifier.width(Dimens.space))
                Column(Modifier.weight(1f)) {
                    Text(
                        "${result.totalBacklogs} active backlog(s)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        "Subjects awaiting a clear across ${result.semesters.size} semester(s).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        result.semesters.forEach { semester ->
            SemesterCard(
                semester = semester,
                showSgpa = false,
                dashForZeroMarks = true
            )
        }
    }
}
