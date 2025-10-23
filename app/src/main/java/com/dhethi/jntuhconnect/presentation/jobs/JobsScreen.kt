package com.dhethi.jntuhconnect.presentation.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JobsScreen() {

    Column(
        modifier = Modifier
            .background(Color.White)

            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Job Opportunities",
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Explore career opportunities and internships",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            Text("Not Yet Implemented", color = Color.LightGray, fontSize = 16.sp)
        }


    }

}