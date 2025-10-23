package com.dhethi.jntuhconnect.presentation.studentResult

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.presentation.components.ErrorScreen
import com.dhethi.jntuhconnect.presentation.components.LoadingScreen
import com.dhethi.jntuhconnect.presentation.studentResult.components.AcademicResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.AllResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.BacklogsResults
import com.dhethi.jntuhconnect.presentation.studentResult.components.RenderStudentDetails
import com.dhethi.jntuhconnect.presentation.studentResult.components.RenderTabs
import com.dhethi.jntuhconnect.presentation.studentResult.components.TopBarTitle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StudentResultScreen(
    viewModel: StudentResultViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            title = { TopBarTitle(uiState.rollNumber, navigateBack) }
        )

        when {
            uiState.studentDetails != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        RenderStudentDetails(uiState.studentDetails)
                    }

                    stickyHeader {
                        RenderTabs(
                            selectedTab = uiState.currentTab,
                            setSelectedTab = { tab -> viewModel.setSelectedTab(tab) }
                        )
                    }

                    item {
                        Crossfade(targetState = uiState.currentTab) { tab ->
                            when (tab) {
                                Constants.ALL_RESULTS -> {
                                    AllResults(uiState.allResult ?: emptyList(), uiState.rollNumber)
                                }

                                Constants.ACADEMIC_RESULTS -> AcademicResults(uiState.academicResult)
                                Constants.BACKLOG_RESULTS -> BacklogsResults(uiState.backlogResult)
                                else -> Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Not yet Implemented",
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Thin,
                                        modifier = Modifier
                                            .padding(0.dp, 180.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            uiState.error.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        ErrorScreen(
                            uiState.error,
                            refreshPage = { viewModel.reloadStudentResults() }
                        )
                    }
                }
            }

            uiState.isLoading -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}