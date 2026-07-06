package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.Details
import com.dhethi.jntuhconnect.presentation.components.CgpaRing
import com.dhethi.jntuhconnect.presentation.components.StatTile
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.brandGradient

/** Gradient hero for the student result screen: identity + CGPA ring + key stats. */
@Composable
fun StudentResultHero(
    details: Details,
    academic: AcademicResult?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dark = isSystemInDarkTheme()
    val hasBacklogs = (academic?.backlogs ?: 0) > 0
    val cgpaValue = academic?.cgpa?.toFloatOrNull() ?: 0f
    val centerValue = when {
        academic == null -> "—"
        hasBacklogs -> "—"
        else -> academic.cgpa
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brandGradient(dark))
            .clip(RoundedCornerShape(bottomStart = Dimens.radiusXl, bottomEnd = Dimens.radiusXl))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()

                .padding(horizontal = Dimens.spaceLg)
                .padding(bottom = Dimens.spaceLg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    "Student Result",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(Modifier.height(Dimens.spaceSm))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        details.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Dimens.spaceXs))
                    Text(
                        details.rollNumber,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        details.branch,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "College ${details.collegeCode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                Spacer(Modifier.width(Dimens.space))
                CgpaRing(
                    progress = if (hasBacklogs) 0f else (cgpaValue / 10f),
                    centerValue = centerValue,
                    caption = "CGPA"
                )
            }

            Spacer(Modifier.height(Dimens.space))

            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
                StatTile(
                    value = academic?.let { creditText(it.credits) } ?: "—",
                    label = "Credits",
                    onBrand = true,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    value = academic?.backlogs?.toString() ?: "—",
                    label = "Backlogs",
                    onBrand = true,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    value = academic?.semesters?.size?.toString() ?: "—",
                    label = "Semesters",
                    onBrand = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
