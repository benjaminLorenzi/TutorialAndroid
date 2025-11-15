package com.example.tutorialandroid.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.StringRes
import com.example.tutorialandroid.R

/**
 * BottomNavItem représente un "onglet" de la barre de navigation du bas.
 *
 * C’est une sealed class pour deux raisons :
 *  1) Elle définit un ensemble fermé d’onglets (Home, Posts, etc.)
 *  2) Elle permet d’avoir des objets singletons pour chaque onglet
 *
 * Chaque item contient :
 *  - route : identifiant unique pour la navigation Compose
 *  - labelRes : ID d'une ressource string (localisation)
 *  - icon : icône Material (ImageVector)
 */
sealed class BottomNavItem(
    val route: String,  // Route utilisée par Navigation Compose
    @StringRes val labelRes: Int, // ID de ressource string localisée
    val icon: ImageVector // Icône Material Design affichée dans la barre
) {
    /**
     * Onglet "Home".
     *
     * - route = identifiant de navigation
     * - labelRes = R.string.title_home permet la localisation automatique
     * - icon = icône Material intégrée à Compose (aucun drawable nécessaire)
     */
    object Home : BottomNavItem(
        route = "home",
        labelRes = R.string.title_home,
        icon = Icons.Filled.Home
    )

    /**
     * Onglet "Posts".
     *
     * Même principe que Home :
     * - route = "posts"
     * - labelRes = R.string.title_posts (localisé dans strings.xml)
     * - icon = icône Material
     */
    object Posts : BottomNavItem(
        route = "posts",
        labelRes = R.string.title_posts,
        icon = Icons.Filled.List
    )
}