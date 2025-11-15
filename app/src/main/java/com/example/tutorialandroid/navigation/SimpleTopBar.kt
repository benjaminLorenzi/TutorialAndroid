package com.example.tutorialandroid.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.annotation.StringRes
import androidx.compose.ui.unit.dp

/**
 * SimpleTopBar est un composant d'interface réutilisable qui affiche un titre centré
 * en haut de l'écran.
 *
 * Ce composant est volontairement simple :
 *  - il n'utilise pas TopAppBar (qui peut nécessiter des API Material3 expérimentales)
 *  - il fonctionne partout et est 100% stable
 *  - il s'appuie sur les ressources string pour la localisation
 *
 * @param titleRes ID d'une ressource string (R.string.xxx)
 *        Ce paramètre respecte les bonnes pratiques Android :
 *        - texte localisable
 *        - aucune chaîne en dur dans le code
 *        - converti en texte via stringResource() dans un contexte composable
 */
@Composable
fun SimpleTopBar(@StringRes titleRes: Int) {
    // Container principal de la barre
    // Box permet de centrer facilement le contenu horizontalement et verticalement.
    Box(
        modifier = Modifier
            .fillMaxWidth() // La barre prend toute la largeur de l'écran
            .padding(vertical = 16.dp), // Espace autour du texte (haut/bas)
        contentAlignment = Alignment.Center // Centre le texte dans la Box
    ) {
        // stringResource() récupère la chaîne localisée depuis R.string.xxx
        // ex : "Accueil" en français, "Home" en anglais
        Text(text = stringResource(id = titleRes))
    }
}