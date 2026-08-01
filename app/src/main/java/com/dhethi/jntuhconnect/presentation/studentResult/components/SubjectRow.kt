package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.domain.model.Subject
import com.dhethi.jntuhconnect.presentation.components.GradePill
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/**
 * A readable subject summary with identity and grade first, followed by clearly
 * labelled marks. The labels stay visible so students do not have to decode a
 * compact table or abbreviations.
 */
@Composable
fun SubjectRow(
    subject: Subject,
    modifier: Modifier = Modifier,
    dashForZeroMarks: Boolean = false,
    detailedMarks: Boolean = false
) {
    fun mark(v: Int) = if (dashForZeroMarks && v == 0) "—" else v.toString()

    if (!detailedMarks) {
        CompactSubjectRow(subject, modifier, dashForZeroMarks)
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spaceMd)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectCode,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subject.subjectName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            GradePill(subject.grades)
        }

        Spacer(Modifier.height(Dimens.spaceSm))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(Dimens.radiusSm)
                )
                .padding(horizontal = Dimens.spaceSm, vertical = Dimens.spaceSm),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXs)
        ) {
            ResultMetric("Internal", mark(subject.internalMarks), Modifier.weight(1f))
            ResultMetric("External", mark(subject.externalMarks), Modifier.weight(1f))
            ResultMetric("Total", mark(subject.totalMarks), Modifier.weight(1f))
            ResultMetric("Credits", creditText(subject.credits), Modifier.weight(1f))
        }
    }
}

@Composable
private fun CompactSubjectRow(
    subject: Subject,
    modifier: Modifier = Modifier,
    dashForZeroMarks: Boolean = false
) {
    fun mark(v: Int) = if (dashForZeroMarks && v == 0) "—" else v.toString()
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = subject.subjectCode,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subject.subjectName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(Dimens.spaceXxs))
            Text(
                text = "Int ${mark(subject.internalMarks)}  ·  Ext ${mark(subject.externalMarks)}  ·  Total ${mark(subject.totalMarks)}",
                style = MaterialTheme.typography.labelSmall,
                color = muted
            )
        }
        Spacer(Modifier.width(Dimens.spaceMd))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GradePill(subject.grades)
            Spacer(Modifier.height(Dimens.spaceXxs))
            Text(
                text = "${creditText(subject.credits)} cr",
                style = MaterialTheme.typography.labelSmall,
                color = muted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ResultMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.defaultMinSize(minHeight = 38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

internal fun creditText(c: Double): String =
    if (c % 1.0 == 0.0) c.toInt().toString() else c.toString()
