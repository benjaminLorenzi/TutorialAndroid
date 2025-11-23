package com.example.tutorialandroid.domain
import com.example.tutorialandroid.network.PostDto

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
 * Extension function permettant de convertir un PostDto (provenant de l'API)
 * vers un PostDomain (modèle métier utilisé dans l'application).
 *
 * Cette fonction isole la logique de transformation entre :
 *  - la couche "Data" (DTO provenant du JSON)
 *  - la couche "Domaine" (objets internes pour l’UI et la logique)
 *
 * L’objectif est de ne jamais exposer directement les DTO réseau à l’UI.
 */
fun PostDto.toDomain(): PostDomain {
    return PostDomain(
        id = id,
        userId = userId,
        // on pourrait faire un transformation ici d'ailleurs
        title = title, // title?.capitalize() ?: ""
        body = body
    )
}