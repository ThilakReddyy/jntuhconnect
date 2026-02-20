package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CustomButtonBar(
    navController: NavController?,
    currentRoute: String?
) {

    BottomAppBar(
        tonalElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                ambientColor = MaterialTheme.colorScheme.primary ,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        containerColor = MaterialTheme.colorScheme.background,
        contentPadding = PaddingValues(0.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                BottomNavItem(
                    label = item.label,
                    icon = item.icon,
                    isSelected = currentRoute == item.route,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = {
                        if (currentRoute != item.route) {
                            navController?.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val tint = when {
        isSelected && isDark -> MaterialTheme.colorScheme.primary
        isSelected -> Color.Black
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        drawLine(
                            color = tint,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                    }
                } else {
                    Modifier
                }
            ),

        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonBarPreview() {
    // Fake or dummy NavController for preview
    val fakeNavController = rememberNavController()

    MaterialTheme {
        CustomButtonBar(
            navController = fakeNavController,
            currentRoute = "home" // simulate "home" being selected
        )
    }
}