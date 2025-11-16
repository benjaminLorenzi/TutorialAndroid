package com.example.tutorialandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * MyExplicitReceiver est un BroadcastReceiver déclenché uniquement par
 * des intents explicites provenant de l'application.
 *
 * Ce composant écoute un Intent envoyé directement par code via
 * `sendBroadcast(intent)` où l'on spécifie explicitement la classe cible.
 *
 * Lorsque le Broadcast est reçu :
 *  - on récupère une éventuelle donnée transmise via getStringExtra("message")
 *  - si elle n'existe pas, on utilise "No message" comme valeur par défaut
 *  - un log est écrit dans Logcat pour confirmer la réception du broadcast
 *
 * L'objectif de ce receiver est éducatif : montrer comment fonctionne
 * un Intent explicite côté Android en ciblant directement un composant
 * (contrairement aux intents implicites plus courants).
 *
 * Pour observer les logs associés :
 *  - ouvrir Logcat dans Android Studio
 *  - filtrer par "MyExplicitReceiver" (tag du Log.d)
 */
class MyExplicitReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message") ?: "No message"
        Log.d("MyExplicitReceiver", "Broadcast received : $message")
    }
}