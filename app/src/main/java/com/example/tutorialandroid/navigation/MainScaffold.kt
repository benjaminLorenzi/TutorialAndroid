package com.example.tutorialandroid.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tutorialandroid.screen.HomeScreen
import com.example.tutorialandroid.screen.PostListScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

// Déclaration du composable principal de l'application.
@Composable
fun MainScaffold() {
    // Création du contrôleur de navigation
    val navController = rememberNavController()
    // Liste de tes onglets (tabs) présents dans la bottom bar.
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Posts
    )

    // On observe UNE FOIS la route actuelle
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        // 🔹 TOP BAR (en haut)
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (currentRoute) {
                        BottomNavItem.Home.route -> "Accueil"
                        BottomNavItem.Posts.route -> "Articles"
                        else -> ""
                    }
                )
            }
        },

        // 🔹 BOTTOM BAR (en bas)
        bottomBar = {
            NavigationBar {
                // On observe la route actuellement affichée pour savoir quel onglet est sélectionné.
                items.forEach { item ->
                    NavigationBarItem(
                        // Coloration automatique du bouton sélectionné.
                        selected = currentRoute == item.route,
                        onClick = {
                            // Navigation vers le nouvel écran quand on clique dessus.
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // launchSingleTop empêche l’ajout du même écran au-dessus de lui-même
                                restoreState = true
                                // restoreState restaure l’état quand tu reviens sur un onglet
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost c'est le root controller"
        // Il affiche l’écran correspondant à l’onglet.
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // si route = home → afficher HomeScreen()
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            // posts → afficher PostListScreen()
            composable(BottomNavItem.Posts.route) {
                PostListScreen()
            }
        }
    }
}
