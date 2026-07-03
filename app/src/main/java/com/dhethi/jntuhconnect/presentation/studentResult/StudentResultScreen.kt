package com.dhethi.jntuhconnect.presentation.studentResult

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.ErrorScreen
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.SegmentedTabs
import com.dhethi.jntuhconnect.presentation.components.ShimmerList
import com.dhethi.jntuhconnect.presentation.studentResult.components.AcademicResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.AllResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.BacklogsResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.CreditsContent
import com.dhethi.jntuhconnect.presentation.studentResult.components.StudentResultHero
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentResultScreen(
    viewModel: StudentResultViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.studentDetails != null -> {
                val details = state.studentDetails
                val listState = rememberLazyListState()
                // Once the hero (item 0) scrolls off, the tabs pin to the top and would
                // sit under the system status bar — inset them by the status bar height then.
                val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                val heroPinned by remember {
                    derivedStateOf { listState.firstVisibleItemIndex >= 1 }
                }
                val headerTopInset by animateDpAsState(
                    targetValue = if (heroPinned) statusBarTop else 0.dp,
                    label = "headerTopInset"
                )
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    item {
                        StudentResultHero(
                            details = details,
                            academic = state.academicResult,
                            onBack = navigateBack
                        )
                    }
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = headerTopInset)
                                .padding(horizontal = Dimens.space, vertical = Dimens.spaceSm)
                        ) {
                            SegmentedTabs(
                                options = Constants.STUDENT_RESULT_TABS,
                                selected = state.currentTab,
                                onSelect = viewModel::setSelectedTab
                            )
                        }
                    }
                    item {
                        AnimatedContent(
                            targetState = state.currentTab,
                            transitionSpec = {
                                (fadeIn(tween(200)) togetherWith fadeOut(tween(150)))
                            },
                            label = "tabContent"
                        ) { tab ->
                            TabContent(tab, state) { viewModel.setSelectedTab(it) }
                        }
                    }
                    item { Spacer(Modifier.height(Dimens.spaceXxl)) }
                }
            }

            state.error.isNotEmpty() -> {
                BackHeader(navigateBack)
                ErrorScreen(state.error, refreshPage = { viewModel.reloadStudentResults() })
            }

            else -> {
                BackHeader(navigateBack)
                ShimmerList(count = 4)
            }
        }
    }
}

@Composable
private fun TabContent(
    tab: String,
    state: StudentResultState,
    onRetry: (String) -> Unit
) {
    when (tab) {
        Constants.ACADEMIC_RESULTS -> AcademicResults(state.academicResult)

        Constants.ALL_RESULTS -> when {
            state.allLoading -> ShimmerList(count = 2)
            state.allError.isNotEmpty() -> InlineError(state.allError) { onRetry(Constants.ALL_RESULTS) }
            else -> AllResults(state.allResult ?: emptyList(), state.rollNumber)
        }

        Constants.BACKLOG_RESULTS -> when {
            state.backlogLoading -> ShimmerList(count = 2)
            state.backlogError.isNotEmpty() -> InlineError(state.backlogError) { onRetry(Constants.BACKLOG_RESULTS) }
            else -> BacklogsResults(state.backlogResult)
        }

        Constants.CREDIT_RESULTS -> when {
            state.creditsLoading -> ShimmerList(count = 2)
            state.creditsError.isNotEmpty() -> InlineError(state.creditsError) { onRetry(Constants.CREDIT_RESULTS) }
            else -> CreditsContent(state.creditsResult)
        }
    }
}

@Composable
private fun InlineError(message: String, onRetry: () -> Unit) {
    AppCard(modifier = Modifier.padding(Dimens.space)) {
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Dimens.spaceMd))
        PrimaryButton(text = "Retry", onClick = onRetry)
    }
}

@Composable
private fun BackHeader(navigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(Dimens.spaceXs)
    ) {
        IconButton(onClick = navigateBack, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
        }
        Text(
            "Student Result",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 40.dp)
        )
    }
}
