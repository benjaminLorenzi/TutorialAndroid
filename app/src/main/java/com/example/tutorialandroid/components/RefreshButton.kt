package com.example.tutorialandroid.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

/**
 * RefreshButton affiche un bouton iconique (cercle rempli avec une icône)
 * permettant de déclencher une action de rafraîchissement.
 *
 * @param onClick Fonction appelée lorsque l’utilisateur appuie sur le bouton.
 *
 * Ce composable utilise :
 *  - FilledIconButton : un bouton Material3 spécialement conçu pour contenir des icônes
 *  - Icons.Default.Refresh : l’icône système représentant l’action de rafraîchissement
 *  - contentDescription : description textuelle pour l'accessibilité (TalkBack)
 */
@Composable
fun RefreshButton(onClick: () -> Unit) {
    FilledIconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh" // description accessibilité / screen reader
        )
    }
}