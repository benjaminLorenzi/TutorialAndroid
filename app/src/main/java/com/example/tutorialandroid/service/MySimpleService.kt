package com.example.tutorialandroid.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.SupervisorJob

class MySimpleService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MySimpleService", "Service créé")
    }

    /**
     * onStartCommand est exécuté à chaque fois que le service est démarré via startService().
     *
     * Dans cet exemple, on lance manuellement un Thread pour exécuter la tâche
     * de manière asynchrone, afin de ne jamais bloquer le thread principal (UI thread).
     *
     * Étapes :
     * 1. On démarre un nouveau Thread
     * 2. On extrait un éventuel message depuis l'Intent
     * 3. On exécute une tâche en arrière-plan (simulateWork)
     * 4. Quand la tâche est terminée, on arrête le service avec stopSelf()
     *
     * Le retour START_NOT_STICKY indique à Android que :
     *  - si le service est tué par le système, il NE sera PAS recréé automatiquement
     *  - Android ne réessayera pas de relancer le service avec un Intent nul
     *
     * En d'autres termes : le service ne persiste pas et ne doit pas redémarrer tout seul.
     * C’est l’option recommandée pour les services légers, déclenchés ponctuellement.
     *
     * ⚠️ Note future :
     * Dans une architecture moderne, ce thread manuel pourra être remplacé
     * par une coroutine (via lifecycleScope ou avec un CoroutineScope dédié),
     * ce qui rendra le code plus propre, annulable et mieux intégré à Android.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            val message = intent?.getStringExtra("message") ?: "No message"
            Log.d("MySimpleService", "Service started. Message: $message")

            simulateWork() // tâche simulée (sleep 1 sec)

            stopSelf() // arrêt du service quand la tâche est finie
        }.start()
        // Plusieurs valeurs seraient possible ici.
        // START_NOT_STICKY = ne redémarre pas → ton service est simplement mort
        // START_STICKY = redémarre mais sans intent
        // START_REDELIVER_INTENT = redémarre avec le même intent
        return START_NOT_STICKY
    }

    private fun simulateWork() {
        Log.d("MySimpleService", "Exécution d'une tâche de fond simple…")
        Thread.sleep(1000) // simulation de travail (1 seconde)
        Log.d("MySimpleService", "Tâche terminée !")
    }

    /**
     * Méthode appelée lorsqu’un composant (Activity, Fragment ou autre)
     * demande à se "lier" (bind) à ce service.
     *
     * Un service lié (Bound Service) permet aux composants clients :
     *  - d’obtenir une référence directe au service
     *  - d'appeler ses méthodes publiques
     *  - de maintenir une connexion active tant que la liaison existe
     *
     * Contrairement à un service démarré via startService(), ici le service
     * agit comme un fournisseur d’API interne : il expose un IBinder que les
     * clients utilisent pour communiquer directement avec lui.
     *
     * Dans cet exemple, le service ne supporte pas le mode "bind"
     * (il s’agit d’un simple service démarré), donc nous retournons null.
     *
     * Si tu voulais créer un vrai Bound Service, tu devrais :
     *  - créer une classe interne LocalBinder : IBinder
     *  - renvoyer cette instance ici
     *  - et gérer la connexion depuis l’Activity via bindService()
     */
    override fun onBind(intent: Intent?): IBinder? {
        // Pas de binding dans cet exemple
        return null
    }
}