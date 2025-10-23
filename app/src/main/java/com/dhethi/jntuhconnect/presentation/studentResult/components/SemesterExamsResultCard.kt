package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.domain.model.ExamResult
import com.dhethi.jntuhconnect.domain.model.SemesterResult
import com.dhethi.jntuhconnect.domain.model.Subject
import com.dhethi.jntuhconnect.presentation.components.buildResultUrl
import com.dhethi.jntuhconnect.presentation.components.getGradeColors
import com.dhethi.jntuhconnect.presentation.components.openCustomTab

@Composable
fun SemesterExamsResultCard(semesterResult: SemesterResult, rollNumber: String) {
    val context = LocalContext.current;
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
                semesterResult.semester + " Results",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,    // Border thickness
            color = Color.Gray // Border color
        )
        semesterResult.exams.forEach { examResult: ExamResult ->
            HorizontalDivider(
                thickness = 1.dp,    // Border thickness
                color = Color.Gray // Border color
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {


                Text(
                    "ExamCode: " + examResult.examCode,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    "Direct Link",
                    color = Color.Black,
                    fontWeight = FontWeight.Thin,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable() {
                            val url = buildResultUrl(rollNumber, examResult)
                            openCustomTab(context, url)
                        }
                )

            }

            HorizontalDivider(
                thickness = 0.5.dp,    // Border thickness
                color = Color.Gray // Border color
            )
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
                    Text("Sub Code", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Internal", fontSize = 12.sp, color = Color.DarkGray)
                    Text("External", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Total", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Grade", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Credits", fontSize = 12.sp, color = Color.DarkGray)

                }
            }
            HorizontalDivider(
                thickness = 0.5.dp,    // Border thickness
                color = Color.Gray // Border color
            )
            examResult.subjects.forEach { subject: Subject ->
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
                        val gradeColors = getGradeColors(subject.grades)
                        Text(
                            subject.subjectCode,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            subject.internalMarks.toString(),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            subject.externalMarks.toString(),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            subject.totalMarks.toString(),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = subject.grades,
                            color = gradeColors.textColor,
                            fontSize = 12.sp,


                        )
                        Text(
                            subject.credits.toString(),
                            fontSize = 12.sp,
                            color = Color.DarkGray
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
        }
    }
}