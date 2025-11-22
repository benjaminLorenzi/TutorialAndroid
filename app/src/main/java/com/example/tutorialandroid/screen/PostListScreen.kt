package com.example.tutorialandroid.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tutorialandroid.R
import com.example.tutorialandroid.data.PostDto
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight
import kotlin.Int

/**
 * Écran affichant une liste de posts (sans appel réseau pour l’instant).
 * Cet écran utilise une Column simple avant d’afficher une liste simulée
 * de données via FakePostList().
 */
@Composable
fun PostListScreen() {
    Column(
        // Espacement vertical automatique entre chaque élément de la colonne
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Petite zone d’accueil ou de contextualisation
        Box(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(id = R.string.welcome_post))
        }
        // Affiche une liste "fake" avant de brancher Retrofit plus tard
        FakePostList()
    }
}

/**
 * Faux fournisseur de données.
 *
 * Génère deux objets PostDto "en dur" pour simuler un résultat d'API REST.
 * Cette fonction est temporaire et sera remplacée plus tard par Retrofit.
 */
@Composable
fun FakePostList() {
    val post1 = PostDto(
        userId = 1,
        id = 1,
        title = "Post1",
        body = "description de mon machin"
    )
    val post2 = PostDto(
        userId = 2,
        id = 2,
        title = "Post2",
        body = "description de mon machin2"
    )
    // Crée une liste immuable de posts
    val posts = listOf(post1, post2)

    // Envoie la liste à l'affichage via PostsList()
    PostsList(posts = posts)
}

/**
 * Composable responsable de l’affichage réel d’une liste de posts.
 *
 * Utilise LazyColumn pour un rendu performant, même avec beaucoup d’entrées.
 * Chaque post est affiché dans une ElevatedCard stylisée.
 */
@Composable
private fun PostsList(posts: List<PostDto>) {
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

            // Carte Material3 pour chaque post
            ElevatedCard(Modifier.fillMaxWidth()) {

                Column(Modifier.padding(16.dp)) {

                    Text("TITRE")

                    // Affichage du titre stylisé
                    Text(
                        post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text("DESCRIPTION")

                    // Affichage du contenu du post
                    Text(
                        post.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}