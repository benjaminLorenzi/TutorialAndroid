package com.example.tutorialandroid.network

import com.example.tutorialandroid.network.PostAPI

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * NetworkPost est un objet singleton responsable de configurer :
 *
 *  - OkHttp (client HTTP bas niveau)
 *  - Logging HTTP
 *  - Retrofit (gestion du REST)
 *  - Moshi (conversion JSON → Kotlin data classes)
 *
 * Cet objet fournit ensuite une instance unique de PostAPI
 * via un accès lazy : NetworkPost.api
 *
 * Ce pattern est très courant et constitue la base des couches réseau
 * dans les applications Android modernes.
 */
object NetworkPost {

    /**
     * Intercepteur de logs HTTP.
     *
     * - Level.BODY → affiche *tout* le contenu des requêtes/réponses :
     *      - URL
     *      - headers
     *      - JSON envoyé / reçu
     * - Très utile en développement mais à désactiver en production.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Configuration du client OkHttp utilisé par Retrofit.
     *
     * - Ajout du logging interceptor
     * - Timeout personnalisés pour les connexions lentes
     * - Client final fourni à Retrofit
     */
    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    /**
     * Instance Moshi configurée pour fonctionner avec les data classes Kotlin.
     *
     * KotlinJsonAdapterFactory permet :
     *  - de lire les valeurs par défaut
     *  - de gérer les nullables
     *  - de mapper automatiquement JSON → Kotlin
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Configuration globale de Retrofit :
     *
     * - baseUrl : URL du serveur (API JSONPlaceholder dans cet exemple)
     * - client Http personnalisé
     * - converter Moshi pour la sérialisation JSON
     */
    private val retrofit = Retrofit.Builder()
        // le vrai serveur
        .baseUrl("https://jsonplaceholder.typicode.com/")
        //.baseUrl("http://10.0.2.2:3010/")
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    /**
     * Exposition de l’API PostAPI.
     *
     * Le `by lazy` garantit :
     * - une seule instance créée à la première utilisation
     * - pas d’instanciation inutile au démarrage de l’app
     */
    val api: PostAPI by lazy {
        retrofit.create(PostAPI::class.java)
    }
}
