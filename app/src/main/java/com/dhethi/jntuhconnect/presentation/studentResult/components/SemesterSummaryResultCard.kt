package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.domain.model.Semester
import com.dhethi.jntuhconnect.domain.model.Subject

@Composable
fun SemesterSummaryResultCard(semesterSummary: Semester, showSGPA: Boolean = true) {
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
                .background(Color.Black)
                .padding(8.dp)


        ) {
            Text(
                "${semesterSummary.semester} Results",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sub Code",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Internal",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "External",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Total",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Grade",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Credits",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,    // Border thickness
            color = Color.Gray // Border color
        )
        semesterSummary.subjects.forEach { subject: Subject ->
            HorizontalDivider(
                thickness = 0.5.dp,    // Border thickness
                color = Color.Gray // Border color
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        subject.subjectCode,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (subject.internalMarks == 0) "-" else subject.internalMarks.toString(),

                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (subject.externalMarks == 0) "-" else subject.externalMarks.toString(),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (subject.totalMarks == 0) "-" else subject.totalMarks.toString(),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        subject.grades,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        subject.credits.toString(),
                        fontSize = 12.sp,
                        color = Color.Black
                    )

                }
                Text(
                    subject.subjectName,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (showSGPA) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)

            ) {
                Row(
                    modifier = Modifier
                        .border(
                            1.dp, Color.LightGray

                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .background(Color.Black)
                            .padding(8.dp)


                    ) {
                        Text(
                            "SGPA",
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
                            semesterSummary.semesterSGPA.toString(),
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

            }
        }
    }
}
