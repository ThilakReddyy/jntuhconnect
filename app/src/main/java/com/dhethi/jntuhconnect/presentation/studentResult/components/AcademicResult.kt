package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.components.EmptyState

@Composable
fun AcademicResults(studentResult: AcademicResult?) {
    val result = studentResult ?: return
    if (result.semesters.isEmpty()) {
        EmptyState(
            icon = Icons.Rounded.School,
            title = "No academic results",
            subtitle = "No semester results are available for this student yet."
        )
        return
    }
    Column(
        modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        Text(
            text = "Your consolidated best attempt and semester performance.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        result.semesters.forEachIndexed { index, semester ->
            SemesterCard(
                semester = semester,
                showSgpa = true,
                initiallyExpanded = index == 0
            )
        }
    }
}
