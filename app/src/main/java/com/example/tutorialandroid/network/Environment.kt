package com.example.tutorialandroid.network
import android.content.Context

/**
 * Constantes privées pour la gestion des préférences.
 * "private" empêche l'accès à cet objet depuis l'extérieur de ce fichier.
 */
private object SettingsManager {
    const val PREFERENCE_KEY = "MesPreferences"
    const val DEFAULT_BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val KEY_BASE_URL = "base_url"
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
     * 1. Regarde si une URL personnalisée existe dans les préférences.
     * 2. Si oui, la retourne.
     * 3. Si non (ou vide), retourne l'URL par défaut définie dans SettingsManager.
     *
     * @return L'URL valide à utiliser (String non-nulle).
     */
    fun getBaseUrl(): String {
        val sharedPref = context.getSharedPreferences(SettingsManager.PREFERENCE_KEY, Context.MODE_PRIVATE)

        val savedUrl = sharedPref.getString(SettingsManager.KEY_BASE_URL, null)

        // Si savedUrl est null ou vide, on renvoie la valeur par défaut
        return if (savedUrl.isNullOrEmpty()) {
            SettingsManager.DEFAULT_BASE_URL
        } else {
            savedUrl
        }
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