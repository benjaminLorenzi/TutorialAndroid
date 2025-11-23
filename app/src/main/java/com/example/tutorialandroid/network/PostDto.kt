package com.example.tutorialandroid.network

import com.example.tutorialandroid.database.PostEntity

/**
 * Représente un "Post" tel qu’il est renvoyé par l’API JSON
 * (par exemple : https://jsonplaceholder.typicode.com/posts).
 *
 * Cette data class sert principalement de modèle pour le parsing JSON
 * effectué par une librairie de sérialisation (Moshi, Gson, etc.).
 *
 * Chaque propriété correspond directement à une clé du JSON :
 *
 * {
 *   "userId": 1,
 *   "id": 1,
 *   "title": "Titre du post",
 *   "body": "Contenu du post"
 * }
 *
 * L’objectif est de fournir une structure typée, simple à manipuler dans l’UI,
 * tout en assurant une correspondance parfaite avec le payload renvoyé par l’API.
 */
data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

/**
 * Fonction d'extension (Extension Function) pour la classe PostDto.
 * Elle permet de convertir un objet reçu du réseau (DTO) en objet stockable en base (Entity).
 *
 *
 * - 'PostDto' (Data Transfer Object) : C'est ton objet Retrofit, l'image exacte du JSON.
 * - 'PostEntity' : C'est ton objet Room, l'image exacte de ta table SQL.
 */
fun PostDto.toEntity(): PostEntity =
    PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body
    )