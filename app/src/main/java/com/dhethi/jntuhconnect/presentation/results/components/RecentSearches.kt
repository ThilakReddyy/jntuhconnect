package com.dhethi.jntuhconnect.presentation.results.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity

@Composable
fun RecentSearches(
    students: List<StudentDetailsEntity>,
    onAllStudentsDelete: () -> Unit,
    navigate: (route: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(8.dp))
    ClearSearchHistoryDialog(
        showDialog = showDialog,
        onConfirm = {
            onAllStudentsDelete()
            showDialog = false
        },
        onDismiss = {
            showDialog = false
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Row(

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Recent Searches",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontStyle = FontStyle.Italic
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
                .clickable {
                    showDialog = true
                },
        ) {

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Clear history",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { showDialog = true }
            )

        }


    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {

        students.forEach { student ->


            Box(
                modifier = Modifier
                    .fillMaxWidth()

                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = true,
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        {
                            Text(
                                student.name
                                    .split(" ")
                                    .filter { it.isNotBlank() }
                                    .takeLast(2)
                                    .joinToString("")
                                    { it.take(1).uppercase() },
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )

                        }
                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row {
                                Text(
                                    student.name,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Roll No: ${student.rollNumber}",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    student.branch,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        thickness = 0.5.dp,    // Border thickness
                        color = MaterialTheme.colorScheme.secondary // Border color
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Semester",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                student.semester.toString(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,

                                )

                        }

                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "CGPA",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp,

                                )
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (student.backlogs > 0) {
                                    "-"
                                } else {
                                    student.cgpa
                                },
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,

                                )


                        }

                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Backlogs",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp,

                                )
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                (student.backlogs).toString(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,

                                )

                        }


                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun ClearSearchHistoryDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Clear Search History")
            },
            text = {
                Text("Do you want to clear the searches?")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
    }
}