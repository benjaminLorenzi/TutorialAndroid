package com.example.tutorialandroid.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tutorialandroid.R

/**
 * DetailScreen affiche un écran contenant plusieurs actions utilisant
 * les Intents Android : ouvrir un navigateur, composer un numéro,
 * ou créer un email.
 */
@Composable
fun DetailScreen() {
    // Mise en page verticale centrée
    Column(
        modifier = Modifier.fillMaxSize(), // L'écran occupe tout l'espace disponible
        horizontalAlignment = Alignment.CenterHorizontally,  // Centre horizontalement les éléments
        verticalArrangement = Arrangement.Center // Centre verticalement la colonne
    ) {
        Text(text = stringResource(id = R.string.detail_title))
        Spacer(modifier = Modifier.height(32.dp))
        OpenBrowserButton()
        CallButton()
        EmailButton()
        ShareTextButton()
    }
}

/**
 * Ouvre un navigateur via un Intent ACTION_VIEW pointant sur une URL.
 * Démonstration de l'intégration d'Android Intents dans Compose.
 */
@Composable
private fun OpenBrowserButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.google.com")
        }
        context.startActivity(intent)
    }) {
        Text(stringResource(id = R.string.detail_button_open_url))
    }
}

/**
 * Ouvre l'application Téléphone en mode "composer un numéro"
 * grâce à l'Intent ACTION_DIAL.
 *
 * On ne demande pas de permission car ACTION_DIAL ne passe pas l'appel,
 * il ouvre seulement l'écran du dialer.
 */
@Composable
fun CallButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:123456789")
        }
        context.startActivity(intent)
    }) {
        Text(stringResource(id = R.string.detail_button_open_dial))
    }
}

/**
 * Ouvre l'application Mail avec un destinataire pré-rempli
 * via un Intent ACTION_SENDTO.
 *
 * Ce type d'Intent utilise un URI "mailto:" et peut également inclure
 * des paramètres comme le sujet (EXTRA_SUBJECT).
 */
@Composable
fun EmailButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:example@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Sujet test")
        }
        context.startActivity(intent)
    }) {
        Text(stringResource(id = R.string.detail_button_open_email))
    }
}

/**
 * ShareTextButton affiche un bouton permettant de partager un texte
 * avec d'autres applications installées sur l'appareil (SMS, mail, messageries, etc.).
 *
 * Lors du clic :
 *  - un Intent avec l'action ACTION_SEND est créé et configuré avec un texte simple (`text/plain`)
 *  - cet Intent est ensuite enveloppé dans un "chooser" via Intent.createChooser(...)
 *    afin d'afficher le panneau de partage standard d'Android
 *  - l'utilisateur choisit l'application avec laquelle il souhaite partager le contenu
 *
 */
@Composable
fun ShareTextButton() {
    val context = LocalContext.current
    val shareText = stringResource(id = R.string.detail_button_share_text)

    Button(onClick = {
        // Intent classique de partage
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Voici un texte partagé depuis mon app Jetpack Compose !")
            type = "text/plain" // indique au système le type de contenu partagé
        }

        // Choisir explicitement quelle app gérerait l’intent
        // (ouvre le menu système "Partager avec…")
        val shareIntent = Intent.createChooser(sendIntent, shareText)

        // Lance l'intent via le context Android
        context.startActivity(shareIntent)

        /*
            ⚠️ Pourquoi NE PAS utiliser directement : context.startActivity(sendIntent) ?

            Si tu fais :
                context.startActivity(sendIntent)

            Android peut ouvrir automatiquement UNE SEULE application de partage,
            sans afficher le menu “Partager avec…”.

            Cela arrive si l’utilisateur a déjà sélectionné une app de manière permanente
            pour ce type d’action (via le menu classique Android) :

                • "Toujours"   → Android utilisera cette application automatiquement
                • "Une seule fois" → Android réaffichera la liste jusqu'à ce qu'il choisisse "Toujours"

            ➜ Du coup, l’utilisateur peut être envoyé directement dans une application
              (Gmail, Messages, WhatsApp, etc.) SANS voir le panneau de choix.

            En utilisant :
                Intent.createChooser(sendIntent, "Partager avec…")

            → Tu forces systématiquement l’affichage du menu de partage.
            → L’utilisateur choisit toujours explicitement l’app à utiliser.
            → C’est la bonne pratique pour tout ce qui touche au partage de contenu.
        */
        //context.startActivity(sendIntent)
    }) {
        Text(stringResource(id = R.string.detail_button_share_text))
    }
}