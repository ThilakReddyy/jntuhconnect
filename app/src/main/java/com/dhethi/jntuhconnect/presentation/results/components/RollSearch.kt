package com.dhethi.jntuhconnect.presentation.results.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dhethi.jntuhconnect.R

@Composable
fun RollSearch(
    rollNumber: String,
    onRollChange: (String) -> Unit,
    navigate: (route: String) -> Unit
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)
            .border(
                1.dp, Color.LightGray, RoundedCornerShape(16.dp)

            )
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(R.color.inputGray))
    ) {


        Row(
            Modifier.padding(4.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = rollNumber,
                onValueChange = { onRollChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Enter Roll Number") }

            )
            Box(
                modifier = Modifier
                    .border(
                        1.dp, Color.Gray, RoundedCornerShape(16.dp)

                    )
                    .clip(RoundedCornerShape(16.dp))
                    .height(43.dp)
                    .width(43.dp)
                    .clickable {
                        if (rollNumber.isBlank()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_roll_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (rollNumber.length != 10) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_roll_length),
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            keyboardController?.hide()
                            navigate("studentResults/${rollNumber}")
                        }
                    })
            {
                Icon(
                    imageVector = Icons.Sharp.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .zIndex(2F)
                        .background(Color.Black)
                        .fillMaxSize()
                        .padding(8.dp)

                )
            }
        }

    }
}