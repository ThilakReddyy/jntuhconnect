package com.dhethi.jntuhconnect.presentation.results

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhethi.jntuhconnect.presentation.results.components.RecentSearches
import com.dhethi.jntuhconnect.presentation.results.components.ResultsEmptyScreen
import com.dhethi.jntuhconnect.presentation.results.components.RollSearch

@Composable
fun ResultScreen(
    viewModel: ResultsViewModel = hiltViewModel(),
    navigate: (route: String) -> Unit
) {
    val isDark = isSystemInDarkTheme()


    val uiState = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        RollSearch(
            uiState.rollNumber,
            onRollChange = viewModel::updateRollNumber,
            navigate = { navigate(it) }
        )
        if (uiState.students.isEmpty()) {
            ResultsEmptyScreen()
        } else {
            RecentSearches(
                uiState.students,
                onAllStudentsDelete = viewModel::deleteAllStudentDetails,
                navigate = { navigate(it) }
            )
        }
    }

}


