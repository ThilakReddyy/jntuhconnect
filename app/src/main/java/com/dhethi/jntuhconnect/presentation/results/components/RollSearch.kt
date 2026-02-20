package com.dhethi.jntuhconnect.presentation.results.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val isDark = isSystemInDarkTheme()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 24.dp, 16.dp, 8.dp)
            .border(
                1.dp, Color.Transparent, RoundedCornerShape(8.dp)

            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                ambientColor = MaterialTheme.colorScheme.primary ,
                spotColor = MaterialTheme.colorScheme.primary
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {


        Row(
            Modifier
                .padding(12.dp, 0.dp, 0.dp, 0.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = rollNumber,
                onValueChange = { onRollChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                textStyle = TextStyle(fontSize = 14.sp),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        "Enter your Roll Number",
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp, // 👈 match textField font size
                        lineHeight = 18.sp,
                        color = if (isDark) {
                            Color.White.copy(alpha = 0.4f)
                        } else {
                            Color.Black.copy(alpha = 0.4f)
                        }

                    )
                }

            )
            Box(
                modifier = Modifier
                    .border(
                        1.dp, Color.Transparent, RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)

                    )
                    .clip(RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp))
                    .fillMaxHeight()
                    .width(56.dp)
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
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .zIndex(2F)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .fillMaxSize()
                        .padding(8.dp)

                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun RollSearchPreview() {
    var roll by remember { mutableStateOf("") }
    RollSearch(
        rollNumber = roll,
        onRollChange = { roll = it },
        navigate = {}
    )
}