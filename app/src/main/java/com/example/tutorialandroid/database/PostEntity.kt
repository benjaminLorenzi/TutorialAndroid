package com.example.tutorialandroid.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Entity : Cette annotation dit à Room : "Cette classe représente une TABLE dans la base de données SQLite".
 *
 * tableName = "posts" :
 * Par défaut, Room utiliserait le nom de la classe ("PostEntity") comme nom de table.
 * Ici, on force le nom "posts". C'est plus propre et plus standard en SQL (souvent en minuscules et au pluriel).
 */
@Entity(tableName = "posts")
data class PostEntity(
    /**
     * @PrimaryKey : C'est la colonne la plus importante.
     * Elle indique l'identifiant unique de chaque ligne.
     *
     * - Si tu essaies d'insérer un post avec un ID qui existe déjà, Room va soit planter,
     *   soit écraser l'ancien (selon ta stratégie de conflit, "OnConflictStrategy").
     * - Ici, on utilise 'val id: Int', donc l'ID vient probablement de l'API (le serveur décide de l'ID).
     *   Si c'était une donnée créée localement, on aurait pu ajouter 'autoGenerate = true'.
     */
    @PrimaryKey val id: Int,

    /**
     * Les champs suivants (userId, title, body) deviennent automatiquement
     * des COLONNES dans ta table "posts".
     *
     * Le type Kotlin (Int, String) est converti automatiquement en type SQL (INTEGER, TEXT).
     */
    val userId: Int,
    val title: String,
    val body: String
)