package com.example.tutorialandroid.domain
import kotlinx.coroutines.delay
import com.example.tutorialandroid.network.PostAPI
import com.example.tutorialandroid.network.toEntity
import com.example.tutorialandroid.database.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * L'interface du Repository définit le "Contrat" que doit respecter la couche de données.
 *
 * Pourquoi une interface ?
 * 1. Pour l'abstraction : Le ViewModel ne sait pas si les données viennent d'une DB, d'un fichier ou du réseau.
 * 2. Pour le testing : On peut facilement échanger la "Vraie" implémentation avec une "Fausse" (Fake) pour les tests.
 */
interface PostRepository {
    /**
     * Déclenche la synchronisation des données.
     *
     * Notez qu'elle ne retourne RIEN (Unit).
     * Son seul but est de mettre à jour la base de données locale.
     * L'UI sera notifiée via le Flow de 'getStoredPosts()'.
     */
    suspend fun refreshPosts()
    /**
     * Expose un flux de données continu depuis la base de données.
     *
     * Flow<List<PostDomain>> : "Un tuyau qui envoie des listes de posts".
     * Si la base de données change (via refreshPosts), une nouvelle liste est émise ici automatiquement.
     */
    fun getStoredPosts(): Flow<List<PostDomain>>
}

/**
 * FakePostRepository : Une fausse implémentation pour les Previews Jetpack Compose et les Tests Unitaires.
 * Elle permet d'afficher l'UI sans avoir besoin d'internet ou d'une vraie base de données.
 */
class FakePostRepository : PostRepository {
    // Ne fait rien car on est en mode "Fake"
    override suspend fun refreshPosts() {
    }

    // Retourne immédiatement une liste statique dans un Flow
    override fun getStoredPosts(): Flow<List<PostDomain>> {
        val posts = listOf(
            PostDomain(id = 1, userId = 1, title = "Fake post 1", body = "Description 1"),
            PostDomain(id = 2, userId = 1, title = "Fake post 2", body = "Description 2")
        )
        return flowOf(posts)  // flowOf crée un Flow qui émet une valeur et s'arrête
        /*
        return flow {
            emit(posts)
        } */
    }
}

/**
 * NetworkPostRepository : La "Vraie" implémentation utilisée par l'application.
 * Elle orchestre la logique entre l'API (Réseau) et le DAO (Base de données).
 */
class NetworkPostRepository(
    private val api: PostAPI,
    private val postDao: PostDao
) : PostRepository {

    /**
     * Pattern "Fetch and Save" (Récupérer et Sauvegarder)
     *
     * 1. On appelle l'API (Réseau).
     * 2. On convertit les objets API (DTO) en objets Base de données (Entity).
     * 3. On insère tout dans la base locale via le DAO.
     *
     * Cette fonction est 'suspend' car l'appel réseau et l'écriture disque sont longs.
     */
    override suspend fun refreshPosts() {
        val postsDto = api.fetchPosts()
        val entities = postsDto.map { it.toEntity() }
        // C'est ici que la magie opère : l'insertion va déclencher le Flow ci-dessous
        postDao.insertAll(entities)
    }

    /**
     * Pattern "Observe Local Data" (Observer les données locales)
     *
     * On retourne le Flow du DAO, mais on doit convertir les données au passage.
     * La DB donne des 'PostEntity', mais le ViewModel veut des 'PostDomain'.
     */
    override fun getStoredPosts(): Flow<List<PostDomain>> {
        return postDao.getAll() // Retourne Flow<List<PostEntity>>
            .map { entities ->
                // Opérateur map du Flow : on transforme ce qui sort du tuyau
                // Ici, on transforme la List<Entity> en List<Domain>
                entities.map { it.toDomain() }
            }
    }
}
