package com.dhethi.jntuhconnect.presentation.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhethi.jntuhconnect.presentation.results.components.RecentSearches
import com.dhethi.jntuhconnect.presentation.results.components.ResultScreenHeader
import com.dhethi.jntuhconnect.presentation.results.components.ResultsEmptyScreen
import com.dhethi.jntuhconnect.presentation.results.components.RollSearch

@Composable
fun ResultScreen(
    viewModel: ResultsViewModel = hiltViewModel(),
    navigate: (route: String) -> Unit
) {

    val uiState = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ResultScreenHeader()
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
                navigate = { navigate(it) }
            )
        }
    }

}


