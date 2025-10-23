package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .padding(bottom = 0.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)

        ) {

            Text(
                "Student Information",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {


                Text("Name:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(46.dp))

                Text(
                    studentDetails!!.name,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text("College Code:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    studentDetails!!.collegeCode,
                    color = Color.White,
                    fontSize = 12.sp
                )


            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("Roll Number:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    studentDetails!!.rollNumber,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {


                Text("Branch:", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(43.dp))
                Text(
                    studentDetails!!.branch ,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }


        }
    }
}