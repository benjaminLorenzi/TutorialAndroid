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
import kotlinx.coroutines.flow.collectLatest
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

    /**
     * Bloc init : Exécuté dès la création du ViewModel.
     * On lance immédiatement deux actions en parallèle.
     */
    init {
        // 1. On ouvre le tuyau d'écoute vers la base de données locale.
        observeLocalPosts()
        // 2. On demande au réseau d'aller chercher des données fraîches.
        refresh()
    }

    /**
     * Observe le flux de données local (Room).
     * C'est ici que le pattern "Single Source of Truth" prend tout son sens.
     * L'UI ne réagit QU'AUX changements de la base de données.
     */
    private fun observeLocalPosts() {
        viewModelScope.launch {
            // collectLatest : Si une nouvelle liste arrive alors qu'on traitait la précédente,
            // on annule le traitement en cours et on prend la plus récente.
            repository.getStoredPosts().collectLatest { posts ->
                if (posts.isNotEmpty()) {
                    // Si on a des données en cache, on les affiche immédiatement (Offline First)
                    _uiState.value = PostsUiState.Success(posts)
                } else {
                    // Cas : Cache vide (première installation) ou vidé.
                    // On reste en Loading tant qu'on n'a pas reçu de données,
                    // sauf si on est déjà en erreur.
                    if (_uiState.value !is PostsUiState.Error) {
                        _uiState.value = PostsUiState.Loading
                    }
                }
            }
        }
    }

    /**
     * Fonction générique pour charger les données.
     *
     * @param force Si true, on force un appel réseau même si on affiche déjà des données.
     *              Utile pour le "Pull-to-Refresh".
     */
    fun load(force: Boolean = false) {
        // Optimisation : Si on a déjà les données et qu'on ne force pas, on ne fait rien.
        // Cela évite de spammer le serveur si l'utilisateur tourne son écran (rotation).
        if (!force && _uiState.value is PostsUiState.Success) return

        viewModelScope.launch {
            try {
                // On signale qu'on travaille (utile pour afficher une barre de chargement)
                // Note : Si on a déjà des données (Success), on pourrait choisir de ne pas repasser
                // en Loading complet pour éviter que l'écran ne clignote (c'est un choix UX).
                _uiState.value = PostsUiState.Loading
                // Appel bloquant (suspend) mais asynchrone vers le réseau + écriture DB.
                repository.refreshPosts()
                // SUPER IMPORTANT :
                // On n'émet pas de "Success" ici manuellement !
                // Pourquoi ? Parce que 'repository.refreshPosts()' a écrit dans la DB.
                // La DB a déclenché 'observeLocalPosts()' (plus haut), qui LUI va émettre le Success.
            } catch (e: Exception) {
                // Gestion des erreurs
                android.util.Log.e("PostsViewModel", "refresh failed", e)
                // On informe l'UI qu'il y a eu un pépin
                _uiState.value = PostsUiState.Error(
                    e.message ?: "Erreur lors du rafraîchissement des posts"
                )
            }
        }
    }

    /**
     * Helper pour forcer le rafraîchissement
     */
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