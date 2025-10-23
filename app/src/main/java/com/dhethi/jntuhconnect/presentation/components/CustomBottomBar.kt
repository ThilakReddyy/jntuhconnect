package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CustomButtonBar(
    navController: NavController?,
    currentRoute: String?
) {
    BottomAppBar(
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            , // control height here
        contentPadding = PaddingValues(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
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
        isSelected && isDark -> Color.White
        isSelected -> Color.Black
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() },
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

