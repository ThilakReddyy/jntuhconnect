package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.domain.model.SemesterResult

@Composable
fun AllResults(semesterResults: List<SemesterResult>, rollNumber: String) {
    if (semesterResults.isEmpty()) {
        return;
    }
    Column()
    {
        semesterResults.forEach { semesterResult: SemesterResult ->
            SemesterExamsResultCard(semesterResult, rollNumber)
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}
