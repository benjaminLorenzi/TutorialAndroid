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
import com.example.tutorialandroid.domain.PostDomain
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tutorialandroid.viewModel.PostsUiState
import com.example.tutorialandroid.viewModel.PostsViewModel
import kotlin.Int

/**
 * Écran affichant une liste de posts en fonction de l’état UI exposé par un ViewModel.
 *
 * Le ViewModel (PostsViewModel) expose un StateFlow<PostsUiState> qui représente
 * les différents états possibles de l’écran :
 *  - Loading : les données sont en cours de récupération
 *  - Error   : une erreur est survenue
 *  - Success : les posts ont été chargés avec succès
 *
 * Le composable réagit automatiquement aux changements d’état grâce à collectAsState().
 */
@Composable
fun PostListScreen(
    vm: PostsViewModel = viewModel()
) {
    // Collecte le StateFlow du ViewModel et le convertit en State Compose réactif.
    // Toute mise à jour du ViewModel forcera une recomposition.
    val uiState by vm.uiState.collectAsState()

    /**
     * LaunchedEffect(Unit) :
     * - S'exécute une seule fois lors de la première composition du composable
     * - Permet d'exécuter du code suspendu ou lié au ViewModel sans provoquer de recompositions infinies
     * - Ici, on déclenche le chargement des données lors de l’arrivée sur l’écran
     */
    LaunchedEffect(Unit) {
        vm.load()
    }

    // Mise en page verticale de base
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp) // espace constant entre les éléments
    ) {

        // Zone d'accueil ou d’introduction
        Box(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(id = R.string.welcome_post))
        }

        // Conteneur principal pour l'affichage de l'état UI
        Box() {
            when (val s = uiState) {

                // 🔵 État : chargement
                // Affiche un spinner centré
                is PostsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // 🔴 État : erreur
                // Affiche un message + un bouton "Réessayer"
                is PostsUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Oups : ${s.message}")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { vm.load() }) {
                            Text("Réessayer")
                        }
                    }
                }

                // 🟢 État : succès
                // Affiche la liste réelle des posts
                is PostsUiState.Success -> {
                    PostsList(posts = s.posts)
                }
            }
        }
    }
}


/**
 * Composable responsable de l’affichage réel d’une liste de posts.
 *
 * Utilise LazyColumn pour un rendu performant, même avec beaucoup d’entrées.
 * Chaque post est affiché dans une ElevatedCard stylisée.
 */
@Composable
private fun PostsList(posts: List<PostDomain>) {
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