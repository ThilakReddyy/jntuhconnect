package com.dhethi.jntuhconnect.presentation.contrast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.domain.model.ContrastProfile
import com.dhethi.jntuhconnect.domain.model.ContrastSemester
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.EmptyState
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.RollNumberField
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.components.ShimmerList
import com.dhethi.jntuhconnect.presentation.components.StatusChip
import com.dhethi.jntuhconnect.presentation.components.gradeColor
import com.dhethi.jntuhconnect.presentation.components.isValidRollNumber
import com.dhethi.jntuhconnect.presentation.home.initials
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.brandGradient
import com.dhethi.jntuhconnect.presentation.theme.LocalJntuhDarkTheme

@Composable
fun ResultContrastScreen(
    viewModel: ContrastViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onOpenStudent: (roll: String) -> Unit
) {
    val state = viewModel.state.value
    val secondRollFocus = remember { FocusRequester() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Result Contrast", onBack = navigateBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = Dimens.space,
                end = Dimens.space,
                top = Dimens.spaceSm,
                bottom = Dimens.spaceXxl
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
        ) {
            item {
                AppCard {
                    Text(
                        "Compare two students",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Both must be the same regulation, year and branch.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Dimens.space))
                    RollNumberField(
                        value = state.roll1,
                        onValueChange = viewModel::updateRoll1,
                        label = "First roll number",
                        showSearchAction = false,
                        imeAction = ImeAction.Next,
                        onNext = { secondRollFocus.requestFocus() }
                    )
                    Spacer(Modifier.height(Dimens.spaceMd))
                    RollNumberField(
                        value = state.roll2,
                        onValueChange = viewModel::updateRoll2,
                        modifier = Modifier.focusRequester(secondRollFocus),
                        label = "Second roll number",
                        showSearchAction = false,
                        imeAction = ImeAction.Done,
                        onSubmit = viewModel::compare
                    )
                    Spacer(Modifier.height(Dimens.space))
                    PrimaryButton(
                        text = "Compare",
                        onClick = viewModel::compare,
                        loading = state.isLoading,
                        enabled = isValidRollNumber(state.roll1) &&
                            isValidRollNumber(state.roll2) && state.roll1 != state.roll2,
                        icon = Icons.Rounded.CompareArrows,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (state.error.isNotEmpty()) {
                item {
                    AppCard(containerColor = MaterialTheme.colorScheme.errorContainer) {
                        Text(
                            state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            if (state.isLoading) {
                item { ShimmerList(count = 3) }
            }

            val contrast = state.contrast
            if (contrast != null && contrast.studentProfiles.size == 2) {
                val p1 = contrast.studentProfiles[0]
                val p2 = contrast.studentProfiles[1]
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)) {
                        ProfileCard(p1, Modifier.weight(1f)) { onOpenStudent(p1.rollNumber) }
                        ProfileCard(p2, Modifier.weight(1f)) { onOpenStudent(p2.rollNumber) }
                    }
                }
                item {
                    SectionHeader("Semester comparison", modifier = Modifier.padding(top = Dimens.spaceSm))
                }
                items(contrast.semesters) { pair ->
                    if (pair.size == 2) {
                        SemesterCompareCard(
                            label1 = p1.name.shortLabel(p1.rollNumber),
                            label2 = p2.name.shortLabel(p2.rollNumber),
                            s1 = pair[0],
                            s2 = pair[1]
                        )
                    }
                }
            } else if (contrast == null && !state.isLoading && state.error.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Rounded.CompareArrows,
                        title = "Compare results",
                        subtitle = "Enter two roll numbers above to see a side-by-side comparison."
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: ContrastProfile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val dark = LocalJntuhDarkTheme.current
    AppCard(modifier = modifier, onClick = onClick) {
        Box(
            modifier = Modifier
                .size(Dimens.avatar)
                .clip(CircleShape)
                .background(brandGradient(dark)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                profile.name.initials(),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(Dimens.spaceSm))
        Text(
            profile.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            profile.rollNumber,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(Dimens.spaceSm))
        val cgpa = if (profile.backlogs > 0) "—" else profile.cgpa
        StatusChip("CGPA $cgpa", if (profile.backlogs > 0) MaterialTheme.colorScheme.error else gradeColor("A"))
        Spacer(Modifier.height(Dimens.spaceXs))
        Text(
            "${profile.backlogs} backlog(s) · ${creditsText(profile.credits)} credits",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SemesterCompareCard(
    label1: String,
    label2: String,
    s1: ContrastSemester,
    s2: ContrastSemester
) {
    AppCard {
        Text(
            "Semester ${s1.semester.takeIf { it != "-" } ?: s2.semester}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(Dimens.spaceSm))
        SgpaBar(label1, s1)
        Spacer(Modifier.height(Dimens.spaceSm))
        SgpaBar(label2, s2)
    }
}

@Composable
private fun SgpaBar(label: String, sem: ContrastSemester) {
    val sgpa = sem.semesterSGPA
    val value = sgpa.toFloatOrNull()
    val fraction = ((value ?: 0f) / 10f).coerceIn(0f, 1f)
    val barColor = if (sem.failed) MaterialTheme.colorScheme.error else gradeColor("O")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(72.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(10.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                    .background(barColor)
            )
        }
        Spacer(Modifier.width(Dimens.spaceSm))
        Text(
            if (value != null) sgpa else "—",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End
        )
    }
}

private fun String.shortLabel(fallback: String): String {
    val first = split(" ").firstOrNull { it.isNotBlank() }
    return first ?: fallback.takeLast(4)
}

private fun creditsText(c: Double): String =
    if (c % 1.0 == 0.0) c.toInt().toString() else c.toString()
