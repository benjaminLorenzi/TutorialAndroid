package com.example.tutorialandroid.domain
import com.example.tutorialandroid.network.PostDto
import com.example.tutorialandroid.database.PostEntity

/**
 * Représente un modèle métier (domain model) pour un Post utilisé à l’intérieur de l'application.
 *
 * Contrairement à PostDto (qui reflète exactement la structure JSON de l’API),
 * cette classe est indépendante des formats réseau et peut évoluer sans impacter
 * la couche data ou Retrofit.
 *
 * L’utilisation d’un modèle "Domain" permet :
 *  - de séparer les préoccupations (Clean Architecture)
 *  - d’éviter de propager les modèles techniques (DTO) dans l’ensemble de l’app
 *  - d’adapter et transformer les données réseau au besoin (formatage, fallback, valeurs par défaut…)
 *
 * Pour l’instant, PostDomain est identique à PostDto, mais cette séparation
 * devient très utile dès que des règles métier, formats custom ou champs dérivés apparaissent.
 */
data class PostDomain(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)


/**
 * Fonction d'extension pour convertir une Entité (Base de données) en Modèle du Domaine (Métier/UI).
 *
 * Architecture :
 * PostEntity (Couche Data) -> PostDomain (Couche Domain/UI)
 *
 * Pourquoi ce mapping ?
 * - PostEntity est lié aux contraintes techniques de Room (annotations @Entity, @PrimaryKey...).
 * - PostDomain est un objet pur Kotlin (POJO/Data Class) que l'UI (Jetpack Compose) va afficher.
 *
 * Cela empêche l'UI de connaître l'existence de la base de données.
 */
fun PostEntity.toDomain(): PostDomain =
    PostDomain(
        userId = userId,
        id = id,
        title = title,
        body = body
    )