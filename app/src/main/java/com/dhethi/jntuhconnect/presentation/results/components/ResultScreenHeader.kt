package com.dhethi.jntuhconnect.presentation.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreenHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Student Results",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Enter roll number to search academic records",
            fontSize = 14.sp,
            color = Color(0xFF4B5563),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

}