package com.example.tutorialandroid.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @Database : Cette annotation est obligatoire. Elle déclare que cette classe
 * est le point d'entrée officiel de ta base de données Room.
 *
 * Paramètres de configuration :
 * 1. entities = [PostEntity::class] :
 *    C'est ici qu'on liste TOUTES les tables de notre application.
 *    Si tu crées une autre table (ex: UserEntity), tu devras l'ajouter ici : [PostEntity::class, UserEntity::class].
 *    Room va scanner ces classes pour créer la structure SQL (CREATE TABLE...).
 *
 *
 * 2. version = 1 :
 *    C'est le numéro de version de ton schéma de base de données.
 *    C'est CRUCIAL pour les mises à jour.
 *    - Aujourd'hui tu livres l'app en version 1.
 *    - Demain, si tu ajoutes une colonne "date" à PostEntity, tu DEVRAS passer à version = 2.
 *    - Room saura alors qu'il doit exécuter une "Migration" pour mettre à jour les téléphones des utilisateurs sans effacer leurs données.
 *
 * 3. exportSchema = false :
 *    Room peut générer un fichier JSON qui décrit ta base de données (utile pour le versioning Git du schéma).
 *    On le met souvent à 'false' pour les projets simples pour éviter un warning à la compilation si le dossier n'est pas configuré.
 */
@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Les DAOs (Data Access Objects) :
     * Pour chaque DAO que tu as créé (ici PostDao), tu dois déclarer une méthode abstraite
     * qui ne prend aucun argument et retourne le DAO.
     *
     * Pourquoi abstrait ?
     * Tu n'as pas besoin d'écrire le code de cette méthode ("return new PostDaoImpl...").
     * Room va générer tout le code d'implémentation automatiquement à la compilation.
     *
     * Usage dans l'app :
     * val dao = database.postDao()
     * dao.getAll()
     */
    abstract fun postDao(): PostDao
}