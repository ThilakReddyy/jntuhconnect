package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.dhethi.jntuhconnect.presentation.Screen

data class BottomNavItemData(
    val label: String,
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItemData("Home", Screen.Home.route, Icons.Outlined.Home, Icons.Rounded.Home),
    BottomNavItemData("Explore", Screen.Explore.route, Icons.Outlined.GridView, Icons.Rounded.GridView),
    BottomNavItemData("Profile", Screen.Profile.route, Icons.Outlined.Person, Icons.Rounded.Person)
)
