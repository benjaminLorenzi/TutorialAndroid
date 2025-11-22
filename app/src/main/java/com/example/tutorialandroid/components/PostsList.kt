package com.example.tutorialandroid.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tutorialandroid.domain.PostDomain

/**
 * Composable responsable de l’affichage réel d’une liste de posts.
 *
 * Utilise LazyColumn pour un rendu performant, même avec beaucoup d’entrées.
 * Chaque post est affiché dans une ElevatedCard stylisée.
 */
@Composable
fun PostsList(posts: List<PostDomain>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Utilise tout l’espace
        contentPadding = PaddingValues(12.dp), // Padding autour de la liste
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espacement entre les items
    ) {
        // items() permet de parcourir la liste et de dessiner chaque post
        items(
            posts,
            key = { it.id } // Fournit une clé stable pour optimiser les recompositions
        ) { post ->
            PostView(post = post)
        }
    }
}
