package com.example.tutorialandroid.screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.tutorialandroid.network.Environment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun SettingsScreen(onClickDb: () -> Unit) {
    // 1. On récupère le contexte actuel (nécessaire pour les SharedPreferences)
    val context = LocalContext.current

    // 2. Variable d'état pour le champ de texte
    var textValue by remember { mutableStateOf("") }

    val environment = Environment(context)

    // 3. Charger la donnée sauvegardée au démarrage de l'écran
    LaunchedEffect(Unit) {
        textValue = environment.getBaseUrl()
        Log.d("base_url", textValue)
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Sauvegarde locale")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = { Text("Entrez votre texte") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            environment.setBaseUrl(textValue)
        }) {
            Text("Sauvegarder")
        }

        Button(
            onClick = {
                onClickDb()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Vider la Database (Dev Only)")
        }
    }
}