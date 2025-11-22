package com.example.tutorialandroid.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorialandroid.network.NetworkPost
import com.example.tutorialandroid.domain.NetworkPostRepository
import com.example.tutorialandroid.domain.PostDomain
import com.example.tutorialandroid.domain.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Représente l'état de l'UI pour l'écran des posts.
// On utilise une sealed interface pour avoir un "state machine" clair :
// - Loading  : chargement en cours
// - Success  : données chargées correctement
// - Error    : une erreur est survenue (réseau, parsing, etc.)
sealed interface PostsUiState {
    object Loading : PostsUiState
    data class Success(val posts: List<PostDomain>) : PostsUiState
    data class Error(val message: String) : PostsUiState
}

// ViewModel responsable de fournir l'état des posts à la couche UI.
// Il dépend d'un PostRepository (injection simple via paramètre par défaut).
class PostsViewModel(
    private val repository: PostRepository = NetworkPostRepository(NetworkPost.api) // simple DI
) : ViewModel() {

    // State flow interne, mutable seulement dans le ViewModel.
    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)

    // State flow exposé à l'UI, en lecture seule.
    // La composable observera uiState et se recomposera selon Loading / Success / Error.
    val uiState: StateFlow<PostsUiState> = _uiState

    // Fonction publique pour déclencher le chargement des posts.
    // Le parametre 'force' refais un refresh meme si les donnes sont deja chargees
    fun load(force: Boolean = false) {
        // Petit garde-fou : si on a déjà un succès, on évite de relancer un chargement.
        if (!force && _uiState.value is PostsUiState.Success) return

        // Coroutine attachée au cycle de vie du ViewModel
        viewModelScope.launch {
            _uiState.value = PostsUiState.Loading
            try {
                // Récupération des données via le repository (réseau ou fake, selon l'implémentation)
                val data = repository.fetchPosts()
                _uiState.value = PostsUiState.Success(data)
            } catch (e: Exception) {
                // En cas d'erreur (réseau, parsing, etc.), on expose un état Error
                _uiState.value = PostsUiState.Error(e.message ?: "Erreur réseau")
            }
        }
    }

    // Refresh call 'load' en mode force
    fun refresh() {
        load(force = true)
    }
}