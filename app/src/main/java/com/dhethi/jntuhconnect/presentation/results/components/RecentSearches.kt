package com.dhethi.jntuhconnect.presentation.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity

@Composable
fun RecentSearches(
    students: List<StudentDetailsEntity>, navigate: (route: String) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "History",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text("Recent Searches", fontSize = 18.sp, color = Color.Black)


        }
        students.forEach { student ->

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp, Color.LightGray, RoundedCornerShape(16.dp)

                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = colorResource(R.color.inputGray))
                    .clickable {
                        navigate("studentResults/${student.rollNumber}")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(32.dp))
                                .background(Color.Black)
                        )
                        {
                            Text(
                                student.name
                                    .split(" ")
                                    .take(2).joinToString("") { it.take(1).uppercase() },
                                modifier = Modifier.padding(16.dp),
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )

                        }
                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row {
                                Text(
                                    student.name,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )

                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "Roll No: ${student.rollNumber}",
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))


                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.School,
                                    contentDescription = "History",
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    student.branch,
                                    color = Color.DarkGray,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        thickness = 0.5.dp,    // Border thickness
                        color = Color.Gray // Border color
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Semester", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                student.semester.toString(),
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }

                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("CGPA", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (student.backlogs > 0) {
                                    "-"
                                } else {
                                    student.cgpa
                                },
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )


                        }

                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Backlogs", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                (student.backlogs).toString(),
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }


                    }
                }

            }
        }
    }
}