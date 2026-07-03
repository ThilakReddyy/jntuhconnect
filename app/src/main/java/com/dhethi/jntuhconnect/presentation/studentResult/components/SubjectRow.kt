package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.dhethi.jntuhconnect.domain.model.Subject
import com.dhethi.jntuhconnect.presentation.components.GradePill
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/**
 * Redesigned subject row: code + name on the left, grade pill + credits on the right,
 * with a compact marks line beneath. Replaces the old dense table cells.
 */
@Composable
fun SubjectRow(
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
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subject.subjectName,
                style = MaterialTheme.typography.bodySmall,
                color = muted,
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

internal fun creditText(c: Double): String =
    if (c % 1.0 == 0.0) c.toInt().toString() else c.toString()
