package com.example.tutorialandroid.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tutorialandroid.domain.PostDomain

/**
 * PostView est un composable responsable de l’affichage d’un post unique.
 *
 * Il utilise une ElevatedCard pour créer un conteneur visuel propre,
 * puis expose les deux éléments principaux d’un PostDomain :
 *  - son titre
 *  - sa description (body)
 *
 * Le contenu est structuré dans une Column avec un padding interne,
 * et quelques Text labels (“TITRE”, “DESCRIPTION”) servent de repères visuels
 * pour la lecture. Le titre est stylisé avec MaterialTheme.typography.titleMedium
 * et mis en gras pour plus de hiérarchie visuelle.
 *
 * Ce composant est typiquement utilisé dans une LazyColumn pour afficher
 * une liste d’articles ou de posts.
 */
@Composable
fun PostView(post: PostDomain) {
    // Conteneur visuel principal avec élévation Material Design
    ElevatedCard(Modifier.fillMaxWidth()) {
        // Layout vertical avec marge interne
        Column(Modifier.padding(16.dp)) {
            // Label textuel (non localisé dans cet exemple)
            Text("TITRE")

            // Titre du post, stylisé et mis en valeur
            Text(
                post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            // Petit espace vertical entre les éléments
            Spacer(Modifier.height(6.dp))

            // Second label
            Text("DESCRIPTION")

            // Corps du post (contenu principal)
            Text(
                post.body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}