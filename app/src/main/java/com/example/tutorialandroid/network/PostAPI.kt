package com.example.tutorialandroid.network

import com.example.tutorialandroid.dto.PostDto
import retrofit2.http.GET

/**
 * PostAPI représente l’interface Retrofit permettant de communiquer
 * avec l’endpoint "/posts" de l’API JSONPlaceholder.
 *
 * Chaque fonction de cette interface correspond à une requête HTTP.
 * Retrofit génère automatiquement l’implémentation au runtime.
 *
 * Caractéristiques principales :
 * - L’annotation @GET("posts") indique une requête HTTP GET vers l’URL /posts.
 * - La fonction est "suspend" : elle s’exécute de manière asynchrone grâce aux coroutines.
 * - Le retour est automatiquement désérialisé par Moshi en une liste de PostDto.
 *
 * Exemple de requête complète générée par Retrofit :
 * GET https://jsonplaceholder.typicode.com/posts
 *
 * Cette interface constitue la couche la plus basse dans l’architecture “network”.
 * Elle n’a aucune logique : elle se contente de décrire les endpoints.
 */
interface PostAPI {
    /**
     * Récupère la liste complète des posts depuis l’API.
     * Fonction suspendue : doit être appelée depuis une coroutine.
     *
     * @return List<PostDto> Liste d’objets PostDto désérialisés depuis le JSON.
     */
    @GET("posts")
    suspend fun fetchPosts(): List<PostDto>
}
