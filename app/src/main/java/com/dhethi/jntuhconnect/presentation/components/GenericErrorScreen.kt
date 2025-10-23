package com.dhethi.jntuhconnect.presentation.components


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GenericErrorScreen(
    errorMessage: String? = null,
    refreshPage: () -> Unit
) {
    val tips = listOf(
        "Try refreshing the page",
        "Check your internet connection",
        "Restart the app if the problem persists"
    )

    Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    1.dp, Color.LightGray, RoundedCornerShape(8.dp)
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Icon(
                    imageVector = Icons.Filled.ErrorOutline,
                    contentDescription = "Error Icon",
                    tint = Color(0xFF364152),
                    modifier = Modifier
                        .zIndex(2F)
                        .padding(16.dp)
                        .size(56.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Something Went Wrong",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        errorMessage ?: "An unexpected error occurred. Please try again.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("What you can try:", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                val annotatedText = buildAnnotatedString {
                    tips.forEach { tip ->
                        append("•  $tip\n")
                    }
                }

                Text(
                    text = annotatedText,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Button(
                    onClick = { refreshPage() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Retry Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .zIndex(2F)
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Try Again",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
