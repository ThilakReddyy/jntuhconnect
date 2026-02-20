package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.domain.model.Details

@Composable
fun RenderStudentDetails(studentDetails: Details?) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .padding(bottom = 0.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            )
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(20.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                "Student Information",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {


                Text("Name:", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(46.dp))

                Text(
                    studentDetails!!.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text("College Code:", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    studentDetails!!.collegeCode,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )


            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("Roll Number:", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    studentDetails!!.rollNumber,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {


                Text("Branch:", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(45.dp))
                Text(
                    studentDetails!!.branch ,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }


        }
    }
}