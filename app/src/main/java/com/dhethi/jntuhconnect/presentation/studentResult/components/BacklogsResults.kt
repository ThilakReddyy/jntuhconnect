package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.domain.model.BacklogResult

@Composable
fun BacklogsResults(studentResult: BacklogResult?) {
    if (studentResult === null) {
        return;
    }
    Column() {
        studentResult.totalBacklogs.let {
            if (it > 0) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp, Color.LightGray, RoundedCornerShape(8.dp)

                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .background(Color.Black)
                            .padding(8.dp)


                    ) {
                        Text(
                            "Total Backlogs",
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(8.dp)


                    ) {
                        Text(
                            studentResult.totalBacklogs.toString(),
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp, Color.LightGray, RoundedCornerShape(8.dp)

                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp)


                    ) {
                        Text(
                            "No Backlogs",
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
        Column()
        {
            studentResult.semesters.forEach { semesterSummary ->
                SemesterSummaryResultCard(semesterSummary, false)
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }

}
