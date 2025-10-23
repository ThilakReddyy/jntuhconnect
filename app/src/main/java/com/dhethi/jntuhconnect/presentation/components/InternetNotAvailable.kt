package com.dhethi.jntuhconnect.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifiBad
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
fun InternetNotAvailable(refreshPage:  ()->Unit) {
    val tips = listOf(
        "Check your WiFi or mobile data connection",
        "Try turning airplane mode on and off",
        "Move to an area with better signal"
    )

    Box(modifier = Modifier.fillMaxSize().padding(20.dp))
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp, Color.LightGray, RoundedCornerShape(8.dp)

                    )
            )
            {
                Column(modifier = Modifier.padding(20.dp)) {
                    Icon(
                        imageVector = Icons.Filled.SignalWifiBad,
                        contentDescription = "Search Icon",
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

                        Text("No Internet Connection", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text("Please check your network connection and try again",color=Color.Gray, textAlign = TextAlign.Center)
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
                    Button(onClick = { refreshPage() }, modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,      // background
                        contentColor = Color.White         // text/icon color
                    )) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Search Icon",
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
