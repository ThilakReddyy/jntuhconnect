package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.compositeOver

/**
 * Small colored chip for a grade letter (O, A+, B, F, ...). The tint comes from the
 * shared [gradeColor] mapping so grades read consistently everywhere.
 */
@Composable
fun GradePill(
    grade: String,
    modifier: Modifier = Modifier
) {
    val base = gradeColor(grade)
    val container = base.copy(alpha = 0.16f).compositeOver(MaterialTheme.colorScheme.surface)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(container)
            .defaultMinSize(minWidth = 34.dp)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = grade.ifBlank { "-" },
            color = base,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/** A colored dot for compact grade indication. */
@Composable
fun GradeDot(grade: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(gradeColor(grade))
    )
}

/** Generic soft-tinted status chip (used for backlogs, "new", etc.). */
@Composable
fun StatusChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f).compositeOver(MaterialTheme.colorScheme.surface))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
