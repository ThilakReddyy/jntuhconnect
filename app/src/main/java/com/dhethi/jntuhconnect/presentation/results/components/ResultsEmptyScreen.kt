package com.dhethi.jntuhconnect.presentation.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun ResultsEmptyScreen() {
    Column() {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 62.dp, 0.dp, 24.dp),
            contentAlignment = Alignment.Center

        )
        {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                contentDescription = "Search Icon",
                tint = Color(0xFF364152),
                modifier = Modifier
                    .zIndex(2F)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color(0xFFF3F4F6))
                    .padding(16.dp)
                    .size(36.dp)
            )
        }
        Text(
            "Academic Results",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Search for a roll number to view detailed academic performance",
            fontSize = 14.sp,
            color = Color(0xFF4B5563),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // centers inside Column
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

}