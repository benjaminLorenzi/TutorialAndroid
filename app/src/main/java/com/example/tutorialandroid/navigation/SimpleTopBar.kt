package com.example.tutorialandroid.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.annotation.StringRes
import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import com.example.tutorialandroid.R

/**
 * SimpleTopBar est un composant d'interface réutilisable qui affiche
 * la barre supérieure de l’application.
 *
 * Son comportement s'adapte automatiquement en fonction de l’état de la navigation :
 *
 *  - Si `canNavigateBack` est true :
 *      → la barre affiche une flèche de retour (back arrow)
 *      → suivie du titre aligné à gauche
 *      → un clic sur la flèche déclenche `onNavigateBack()`, généralement un popBackStack()
 *
 *  - Si `canNavigateBack` est false :
 *      → la barre affiche uniquement le titre, centré horizontalement
 *      → utilisé pour les écrans racine (home, onglets de bottom bar)
 *
 * Le titre est fourni sous la forme d'un ID de ressource (`@StringRes`) afin
 * d'assurer une localisation correcte via `stringResource()`.
 *
 * Ce composant est volontairement minimaliste mais respecte les conventions Android :
 *  - bouton back aligné à gauche
 *  - titre affiché dans la top bar
 *  - gestion propre de l’accessibilité via contentDescription
 *
 * Il est conçu pour fonctionner en tandem avec un Scaffold + NavHost,
 * qui déterminent dynamiquement si un écran peut revenir en arrière.
 */
@Composable
fun SimpleTopBar(
    @StringRes titleRes: Int,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit
)
{
    if (canNavigateBack) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_content_description)
                )
            }
            Text(text = stringResource(id = titleRes))
        }
    } else {
        // Titre centré pour les écrans racine
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(id = titleRes))
        }
    }
}
