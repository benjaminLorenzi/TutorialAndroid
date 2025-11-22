package com.example.tutorialandroid.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val repository: PostRepository // simple DI
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
/**
 * Cette classe sert d'usine (Factory) pour fabriquer des instances de PostsViewModel.
 *
 * PROBLÈME À RÉSOUDRE :
 * Par défaut, Android ne sait instancier que des ViewModels qui ont un constructeur vide ().
 * Or, notre PostsViewModel a besoin d'un 'repository' dans son constructeur.
 * Si on ne fait rien, l'application crashera avec une erreur "InstantiationException".
 *
 * SOLUTION :
 * On implémente l'interface `ViewModelProvider.Factory` pour expliquer à Android
 * comment fabriquer manuellement notre ViewModel avec ses dépendances.
 */
class PostsViewModelFactory(
    /*
    Changement de l'architecture du constructeur :
        - Suppression de la valeur par défaut `NetworkPostRepository(NetworkPost.api)`.
        - Le paramètre `repository` est désormais obligatoire.

        Pourquoi ?
        1. Inversion de contrôle : Ce n'est plus au ViewModel de savoir comment créer le Repository.
        2. Testabilité : Cela permet d'injecter facilement un "FakeRepository" lors des tests unitaires, sans déclencher de vrais appels réseau.
        3. Flexibilité : Prépare le terrain pour l'utilisation d'une Factory ou de Hilt.
     */
    private val repository: PostRepository
) : ViewModelProvider.Factory { // On signe le contrat "Je suis une usine à ViewModels"

    /**
     * La méthode 'create' est appelée automatiquement par le framework Android (ViewModelProvider)
     * lorsqu'il a besoin de créer une nouvelle instance du ViewModel.
     *
     * @param modelClass La classe du ViewModel qu'on essaie de créer (ex: PostsViewModel::class.java).
     * @param T Le type générique qui assure que le retour est bien un sous-type de ViewModel.
     */
    @Suppress("UNCHECKED_CAST") // On demande au compilateur d'ignorer l'avertissement de cast (voir plus bas)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // VÉRIFICATION DE SÉCURITÉ :
        // On vérifie si la classe demandée (modelClass) est bien notre PostsViewModel
        // ou une classe qui en hérite.
        // C'est une protection au cas où cette Factory serait utilisée par erreur pour un autre ViewModel.
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {

            // CRÉATION ET INJECTION :
            // C'est le moment clé : on instancie manuellement le ViewModel
            // en lui passant le 'repository' qu'on a stocké dans la Factory.
            // C'est ici que l'injection de dépendance manuelle a lieu.
            val viewModel = PostsViewModel(repository)

            // LE CAST (UNCHECKED CAST) :
            // La méthode doit retourner un type 'T'.
            // Comme on vient de vérifier avec le 'if' que T est bien compatible avec PostsViewModel,
            // on peut forcer le typage avec "as T".
            // L'annotation @Suppress sert à dire "T'inquiète pas Kotlin, je sais ce que je fais".
            return viewModel as T
        }

        // GESTION D'ERREUR :
        // Si quelqu'un essaie d'utiliser cette Factory pour créer un "UserViewModel" par exemple,
        // on lève une exception pour dire "Je ne sais pas fabriquer ça".
        throw IllegalArgumentException("Unknown ViewModel class: Cette factory ne gère que PostsViewModel")
    }
}