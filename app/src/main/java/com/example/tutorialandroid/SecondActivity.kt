package com.example.tutorialandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * SecondActivity est un écran lancé via un Intent explicite.
 *
 * Cette Activity récupère les données envoyées par l'écran précédent
 * (ici : "username") puis affiche son contenu en utilisant un composable
 * Jetpack Compose.
 */
class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupère la valeur envoyée via l'Intent
        // getStringExtra peut renvoyer null → on fournit une valeur par défaut
        val username = intent.getStringExtra("username") ?: "Inconnu"

        // Définition du contenu de l’Activity via Jetpack Compose
        setContent {
            SecondActivityScreen(username = username)
        }
    }
}

/**
 * Composable affichant l'UI de la seconde Activity.
 *
 * @param username la chaîne de caractère reçue de l’Intent et affichée dans la vue.
 */
@Composable
fun SecondActivityScreen(username: String) {
    // Mise en page verticale centrée
    Column(
        modifier = Modifier
            .fillMaxSize() // occupe tout l'écran
            .padding(24.dp), // ajoute des marges sur les côtés
        horizontalAlignment = Alignment.CenterHorizontally, // centre horizontalement
        verticalArrangement = Arrangement.Center  // centre verticalement
    ) {

        // Affiche le titre de l'écran, localisé via stringResource
        Text(
            text = stringResource(R.string.second_activity_welcome)
        )

        Spacer(Modifier.height(12.dp)) // petit espace entre les deux textes

        // Affiche le nom de l'utilisateur reçu depuis l'Intent
        // "%1$s" dans strings.xml est remplacé par `username`
        Text(
            text = stringResource(R.string.second_activity_username, username)
        )
    }
}