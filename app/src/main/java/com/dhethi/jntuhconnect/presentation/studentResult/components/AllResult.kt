package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dhethi.jntuhconnect.domain.model.SemesterResult
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun AllResults(semesterResults: List<SemesterResult>, rollNumber: String) {
    if (semesterResults.isEmpty()) return
    Column(
        modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        semesterResults.forEach { semesterResult ->
            SemesterExamsResultCard(semesterResult, rollNumber)
        }
    }
}
