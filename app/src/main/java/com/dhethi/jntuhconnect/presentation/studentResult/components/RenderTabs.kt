package com.dhethi.jntuhconnect.presentation.studentResult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhethi.jntuhconnect.common.Constants

@Composable
fun RenderTabs(selectedTab: String, setSelectedTab: (String) -> Unit) {
    val tabs = Constants.STUDENT_RESULT_TABS
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF3F4F6)) // light gray background (#F3F4F6)
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f) // equally spaced
                        .clip(RoundedCornerShape(15.dp))
                        .background(if (selectedTab == item) Color.Black else Color.Transparent)
                        .clickable { setSelectedTab(item) } // ✅ FIX: call the callback
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = if (selectedTab == item) Color.White else Color.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}