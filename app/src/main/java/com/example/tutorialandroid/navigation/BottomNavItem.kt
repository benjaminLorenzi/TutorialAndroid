package com.example.tutorialandroid.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        label = "Home",
        icon = Icons.Filled.Home
    )

    object Posts : BottomNavItem(
        route = "posts",
        label = "Posts",
        icon = Icons.Filled.List
    )
}