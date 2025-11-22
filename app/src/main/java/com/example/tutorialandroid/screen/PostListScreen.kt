package com.example.tutorialandroid.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tutorialandroid.R
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tutorialandroid.viewModel.PostsUiState
import com.example.tutorialandroid.viewModel.PostsViewModel
import com.example.tutorialandroid.components.PostsList
import com.example.tutorialandroid.components.RefreshButton
import com.example.tutorialandroid.domain.NetworkPostRepository
import com.example.tutorialandroid.network.NetworkPost
import com.example.tutorialandroid.network.PostAPI

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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(id = R.string.welcome_post))

            RefreshButton(onClick = { vm.refresh() })
        }

        // Conteneur principal pour l'affichage de l'état UI
        Box(modifier = Modifier.fillMaxSize()) {
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
                        Button(onClick = { vm.refresh() }) {
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

