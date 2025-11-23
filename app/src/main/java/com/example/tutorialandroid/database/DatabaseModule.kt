package com.example.tutorialandroid.database

import android.content.Context
import androidx.room.Room

/**
 * DatabaseModule : Objet Singleton responsable de la création de la base de données.
 * Puisque c'est un 'object', ses méthodes sont statiques et accessibles partout.
 */
object DatabaseModule {

    // Singleton : Variable qui va stocker l'unique instance de la base de données.
    // @Volatile : Mot-clé technique important pour le multi-threading.
    // Il garantit que si un thread crée l'instance, tous les autres threads le voient IMMÉDIATEMENT.
    // Sans ça, deux threads pourraient croire que INSTANCE est null en même temps et créer deux bases.
    @Volatile
    private var INSTANCE: AppDatabase? = null

    /**
     * provideDatabase : Méthode pour récupérer l'instance de la DB.
     * Elle utilise le pattern "Double-Check Locking" pour être Thread-Safe.
     */
    fun provideDatabase(context: Context): AppDatabase {
        // 1. Si l'instance existe déjà, on la retourne direct (super rapide).
        // L'opérateur '?:' signifie "si ce qui est à gauche est null, fais ce qui est à droite".
        return INSTANCE ?: synchronized(this) {
            // 2. On entre dans un bloc synchronisé (verrouillé).
            // Un seul thread peut entrer ici à la fois.

            // 3. On revérifie si INSTANCE est null (au cas où un autre thread l'aurait créée
            // juste avant qu'on obtienne le verrou).
            Room.databaseBuilder(
                context.applicationContext, // On utilise applicationContext pour éviter les fuites de mémoire
                AppDatabase::class.java, // La classe abstraite définie précédemment
                "app_database"      // Le nom physique du fichier sur le téléphone (app_database.db)
            )
            // Optionnel : .fallbackToDestructiveMigration() si tu veux écraser la DB quand tu changes la version
            .build().also {
                // 4. Une fois construite, on stocke l'instance dans la variable globale
                INSTANCE = it
            }
        }
    }

    /**
     * providePostDao : Helper pour récupérer directement le DAO sans passer par la DB à chaque fois.
     * C'est souvent ce qu'on injecte dans les ViewModels ou Repositories.
     */
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()
}