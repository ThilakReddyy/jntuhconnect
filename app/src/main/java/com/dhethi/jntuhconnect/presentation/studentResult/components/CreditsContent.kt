package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dhethi.jntuhconnect.domain.model.StudentCreditsResult
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.CgpaRing
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.ProgressBarStat
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun CreditsContent(result: StudentCreditsResult?) {
    val data = result ?: return

    if (!data.available || data.credits == null) {
        EmptyState(
            icon = Icons.Rounded.CreditCard,
            title = "Credits checker unavailable",
            subtitle = data.message
                ?: "This feature is currently available for B.Tech students only."
        )
        return
    }

    val credits = data.credits
    val pct = if (credits.totalRequiredCredits > 0)
        (credits.totalObtainedCredits / credits.totalRequiredCredits).toFloat() else 0f

    Column(
        modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        // Overall summary
        AppCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CgpaRing(
                    progress = pct,
                    centerValue = "${(pct * 100).toInt()}%",
                    caption = "Done",
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    progressColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    ),
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    captionColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(Dimens.space))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Credit progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(Dimens.spaceXs))
                    Text(
                        "${creditText(credits.totalObtainedCredits)} of ${creditText(credits.totalRequiredCredits)} required credits earned",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Dimens.spaceSm))
                    val remaining = (credits.totalRequiredCredits - credits.totalObtainedCredits)
                        .coerceAtLeast(0.0)
                    StatusChip(
                        text = if (remaining <= 0) "On track ✓" else "${creditText(remaining)} to go",
                        color = if (remaining <= 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        // Per-year breakdown
        credits.academicYears.forEachIndexed { index, year ->
            AppCard {
                Text(
                    "Year ${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(Dimens.spaceSm))
                ProgressBarStat(
                    label = "Credits earned",
                    obtained = year.creditsObtained.toFloat(),
                    total = year.requiredForYear.toFloat()
                )
                if (year.semesterWiseCredits.isNotEmpty()) {
                    Spacer(Modifier.height(Dimens.spaceMd))
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSm)) {
                        year.semesterWiseCredits.forEach { (sem, cr) ->
                            StatusChip(
                                text = "Sem $sem · ${creditText(cr)} cr",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
