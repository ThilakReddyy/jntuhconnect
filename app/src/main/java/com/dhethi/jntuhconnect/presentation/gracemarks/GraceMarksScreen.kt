package com.dhethi.jntuhconnect.presentation.gracemarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.RollNumberField
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.studentResult.components.SemesterCard
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun GraceMarksScreen(
    viewModel: GraceMarksViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Grace Marks", onBack = navigateBack) }
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
                        "Grace marks update",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Final-year B.Tech / B.Pharmacy students with pending backlogs can clear " +
                            "them using the grace marks from their Consolidated Marks Memo (CMM).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Dimens.space))
                    RollNumberField(
                        value = state.roll,
                        onValueChange = viewModel::updateRoll,
                        showSearchAction = false
                    )
                    Spacer(Modifier.height(Dimens.space))
                    PrimaryButton(
                        text = "Check eligibility",
                        onClick = viewModel::checkEligibility,
                        loading = state.checkLoading,
                        icon = Icons.Rounded.WorkspacePremium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (state.checkError.isNotEmpty()) {
                item { MessageCard(state.checkError, error = true) }
            }

            val eligibility = state.eligibility
            if (eligibility != null && !state.checkLoading) {
                if (!eligibility.eligible) {
                    item {
                        MessageCard(
                            eligibility.message ?: "You are not eligible for grace marks.",
                            error = false
                        )
                    }
                } else {
                    val backlogs = eligibility.backlogResult
                    item {
                        AppCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.width(Dimens.space))
                                Column {
                                    Text(
                                        "You're eligible 🎉",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        "${backlogs?.totalBacklogs ?: 0} backlog subject(s) may be raised with grace marks.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }

                    // JNTUH grants grace marks only in the offline Consolidated Marks Memo (CMM),
                    // which can't be fetched online — the student uploads it on the website to
                    // have their results recalculated with grace marks.
                    item {
                        AppCard {
                            Text(
                                "Update results with your CMM",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "JNTUH releases grace marks only in the offline Consolidated Marks " +
                                    "Memo (CMM), so they can't be added online automatically. Upload " +
                                    "your CMM on the JNTUH Connect website to update your results with " +
                                    "the grace marks.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(Dimens.space))
                            PrimaryButton(
                                text = "Upload CMM on web",
                                onClick = { openCustomTab(context, "${ContentData.WEB_BASE}/gracemarks") },
                                icon = Icons.AutoMirrored.Rounded.OpenInNew,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Backlog subjects that can be raised
                    if (backlogs != null && backlogs.semesters.isNotEmpty()) {
                        item {
                            Text(
                                "Backlog subjects",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = Dimens.spaceSm)
                            )
                        }
                        items(backlogs.semesters) { semester ->
                            SemesterCard(semester = semester, showSgpa = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageCard(message: String, error: Boolean) {
    val container = if (error) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.surfaceContainerHigh
    val content = if (error) MaterialTheme.colorScheme.onErrorContainer
    else MaterialTheme.colorScheme.onSurface
    AppCard(containerColor = container) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Info, contentDescription = null, tint = content)
            Spacer(Modifier.width(Dimens.space))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = content)
        }
    }
}
