package com.example.tutorialandroid.network
import android.content.Context
import com.example.tutorialandroid.BuildConfig

/**
 * Constantes privées pour la gestion des préférences.
 * "private" empêche l'accès à cet objet depuis l'extérieur de ce fichier.
 */
private object SettingsManager {
    const val PREFERENCE_KEY = "MesPreferences"
    const val KEY_BASE_URL = "base_url"
    // La constante DEFAULT_BASE_URL a été supprimée car elle est remplacée par BuildConfig.API_BASE_URL
}

/**
 * Classe responsable de la gestion de l'environnement (URLs, configurations).
 * Elle fait l'intermédiaire entre l'application et les SharedPreferences.
 *
 * @param context Le contexte nécessaire pour accéder aux fichiers de préférences Android.
 */
class Environment(val context: Context) {

    /**
     * Récupère l'URL de base pour l'API.
     *
     * Logique :
     * 1. En PROD : Force l'URL officielle (sécurité).
     * 2. En DEV : Regarde si une URL perso existe dans les préférences, sinon utilise celle du build.gradle.
     *
     * @return L'URL valide à utiliser (String non-nulle).
     */
    fun getBaseUrl(): String {
        // Si le flavor est "prod", on interdit la modification d'URL
        if (BuildConfig.FLAVOR == "prod") {
            return BuildConfig.API_BASE_URL
        }
        val sharedPref = context.getSharedPreferences(SettingsManager.PREFERENCE_KEY, Context.MODE_PRIVATE)

        val savedUrl = sharedPref.getString(SettingsManager.KEY_BASE_URL, null)

        // Si savedUrl est null ou vide, on renvoie la valeur par défaut
        return savedUrl ?: BuildConfig.API_BASE_URL
    }

    /**
     * Sauvegarde une nouvelle URL de base.
     * Utilise 'apply()' pour une sauvegarde asynchrone sans bloquer l'interface.
     */
    fun setBaseUrl(value: String) {
        val sharedPref = context.getSharedPreferences(SettingsManager.PREFERENCE_KEY, Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString(SettingsManager.KEY_BASE_URL, value)
            apply()
        }
    }
}