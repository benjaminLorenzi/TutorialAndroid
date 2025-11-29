package com.example.tutorialandroid.navigation

import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tutorialandroid.screen.HomeScreen
import com.example.tutorialandroid.screen.DetailScreen
import com.example.tutorialandroid.screen.PostListScreen
import com.example.tutorialandroid.R
import com.example.tutorialandroid.screen.SettingsScreen
import com.example.tutorialandroid.viewModel.PostsViewModel
import com.example.tutorialandroid.BuildConfig

/**
 * MainScaffold est le composable racine de l’application.
 *
 * Il définit la structure générale de l’interface selon les standards Android :
 *
 *  - Une **Top Bar** (header) affichant le titre de l’écran courant.
 *  - Une **Bottom Navigation Bar** permettant de naviguer entre les écrans principaux.
 *  - Un **NavHost** qui gère la navigation et affiche l’écran correspondant à la route active.
 *
 * Fonctionnement :
 *  - `rememberNavController()` initialise le contrôleur de navigation.
 *  - `currentBackStackEntryAsState()` permet d'observer la route actuelle
 *    et donc de mettre à jour la barre du bas et le titre dynamiquement.
 *  - Le `Scaffold` fournit la mise en page globale (top bar, bottom bar, contenu).
 *  - Chaque item de `BottomNavItem` correspond à un onglet de la barre de navigation.
 *
 * Ce composable joue donc un rôle similaire à un "UITabBarController" en iOS,
 * mais adapté aux conventions modernes de Jetpack Compose.
 *
 * Il est conçu pour être :
 *  - simple
 *  - localisable (via R.string)
 *  - facilement scalable (ajout de nouveaux onglets)
 *  - 100% Compose (aucune vue Android classique)
 */
object Routes {
    const val DETAIL = "detail"
}

@Composable
fun MainScaffold(
    vm: PostsViewModel
) {
    // Création du contrôleur de navigation
    val navController = rememberNavController()
    val settingsAvailable = BuildConfig.SHOW_DEBUG_MENU

    // Equivalent
    /*
    val items: MutableList<BottomNavItem> = mutableListOf(BottomNavItem.Home, BottomNavItem.Posts)

    if (settingsAvailable) {
        items.add(BottomNavItem.Settings)
    } */
    val items = buildList {
        add(BottomNavItem.Home)
        add(BottomNavItem.Posts)
        if (settingsAvailable) {
            add(BottomNavItem.Settings)
        }
    }

    val rootRoutes = items.map { it.route }

    // On observe UNE FOIS la route actuelle
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val isRootDestination = currentRoute in rootRoutes

    Scaffold(
        // 🔹 TOP BAR (en haut)
        topBar = {
            SimpleTopBar(
                titleRes = when (currentRoute) {
                    BottomNavItem.Home.route -> R.string.title_home
                    BottomNavItem.Posts.route -> R.string.title_posts
                    BottomNavItem.Settings.route -> R.string.title_settings
                    Routes.DETAIL -> R.string.detail_title
                    else -> R.string.title_home
                },
                canNavigateBack = !isRootDestination, // on n'affiche pas la feche Back '<-' sur les ecrans racines
                onNavigateBack = { navController.navigateUp() }
                // navController.navigateUp fais un pop, retire la vue du navController
            )
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
                                // description textuelle lue par les outils d’accessibilité (comme TalkBack)
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = { Text(stringResource(item.labelRes)) }
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
                HomeScreen(onNavigateToDetail = {
                    navController.navigate(Routes.DETAIL)
                })
            }
            composable(Routes.DETAIL) {
                DetailScreen()
            }

            // posts → afficher PostListScreen()
            composable(BottomNavItem.Posts.route) {
                PostListScreen(vm = vm)
            }
            if (settingsAvailable) {
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen(onClearDb = {
                        vm.clearData()
                    })
                }
            }

        }
    }
}
