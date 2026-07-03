package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.domain.model.SemesterResult
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.buildResultUrl
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun SemesterExamsResultCard(semesterResult: SemesterResult, rollNumber: String) {
    val context = LocalContext.current

    AppCard {
        Text(
            "Semester ${semesterResult.semester}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        semesterResult.exams.forEach { exam ->
            Spacer(Modifier.height(Dimens.spaceMd))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimens.radiusSm))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = Dimens.spaceMd, vertical = Dimens.spaceSm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Exam ${exam.examCode}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (exam.rcrv) {
                    StatusChip("RCRV", MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(Dimens.spaceXs))
                }
                if (exam.graceMarks) {
                    StatusChip("Grace", MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(Dimens.spaceXs))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        openCustomTab(context, buildResultUrl(rollNumber, exam))
                    }
                ) {
                    Text(
                        "Link",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(
                        Icons.AutoMirrored.Rounded.OpenInNew,
                        contentDescription = "Open official result",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(16.dp)
                    )
                }
            }
            exam.subjects.forEachIndexed { index, subject ->
                if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SubjectRow(subject = subject, dashForZeroMarks = false)
            }
        }
    }
}
