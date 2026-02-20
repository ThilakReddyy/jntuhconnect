package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun SmallRoundedButton(
    text: String,
    containerColor: Color,
    textColor: Color,
    onClick: (tab: String) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Button(
        onClick = { onClick(text) },
        modifier = modifier.padding(4.dp,0.dp),
        shape = RoundedCornerShape(10.dp), // smaller rounding, default is 16.dp
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ) {
        Text(text, color = textColor, fontSize = 12.sp, modifier = Modifier.padding(4.dp,0.dp))
    }
}

@Preview(
    name = "Small Rounded Button Preview",
    showBackground = true,
)
@Composable
fun SmallRoundedButtonPreview() {
    MaterialTheme {
        SmallRoundedButton(
            text = "All",
            containerColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.primary,
            onClick = { tab -> println("Clicked: $tab") }
        )
    }
}