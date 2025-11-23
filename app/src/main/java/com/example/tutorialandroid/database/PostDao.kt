package com.example.tutorialandroid.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @Dao (Data Access Object) :
 * Cette annotation dit à Room : "Ceci est le contrat d'interface pour accéder à la table 'posts'."
 * Room va générer automatiquement une classe (PostDao_Impl) qui implémente ces méthodes.
 */
@Dao
interface PostDao {

    /**
     * Lecture des données (READ)
     *
     * @Query : On écrit ici du SQL standard.
     * "SELECT * FROM posts" : On veut toutes les colonnes de toutes les lignes de la table 'posts'.
     *
     * Le type de retour est CRUCIAL ici : Flow<List<PostEntity>>
     * 1. List<PostEntity> : On récupère une liste d'objets.
     * 2. Flow : C'est la partie "Réactive".
     *    Contrairement à une simple List qui est une photo instantanée, un Flow est un tuyau ouvert.
     *    Si tu ajoutes un post dans la base de données ailleurs dans l'app,
     *    ce Flow va AUTOMATIQUEMENT émettre la nouvelle liste mise à jour vers l'UI.
     *    C'est ce qui permet d'avoir une UI toujours synchro avec la DB.
     *
     * Note : Pas besoin de 'suspend' ici car 'Flow' est déjà asynchrone par nature.
     */
    @Query("SELECT * FROM posts")
    fun getAll(): Flow<List<PostEntity>>


    /**
     * Écriture des données (WRITE)
     *
     * @Insert : Demande à Room d'insérer les éléments passés en paramètres.
     *
     * onConflict = OnConflictStrategy.REPLACE :
     * C'est la stratégie du "Cache" (ou Upsert).
     * Si on essaie d'insérer un post avec l'ID 1, et qu'il existe DÉJÀ un post avec l'ID 1 :
     * -> Room va écraser l'ancien avec le nouveau.
     * C'est parfait pour mettre à jour les données locales quand on reçoit des nouvelles données de l'API.
     *
     * suspend : Cette opération d'écriture prend du temps (E/S disque).
     * On oblige l'appelant à utiliser une Coroutine pour ne pas bloquer l'interface (Main Thread).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)
}