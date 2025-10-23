package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.sharp.HomeMax
import androidx.compose.ui.graphics.vector.ImageVector
import com.dhethi.jntuhconnect.presentation.Screen

data class BottomNavItemData(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItemData("Results", Screen.ResultsScreen.route, Icons.Sharp.HomeMax),
    BottomNavItemData("Jobs", Screen.JobsScreen.route, Icons.Outlined.WorkOutline),
    BottomNavItemData("Updates", Screen.UpdatesScreen.route, Icons.Outlined.Notifications),
    BottomNavItemData("Profile", Screen.ProfileScreen.route, Icons.Outlined.Person),
)
