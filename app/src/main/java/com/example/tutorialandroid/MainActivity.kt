package com.example.tutorialandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tutorialandroid.navigation.BottomNavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent { ... } lance Jetpack Compose
        setContent {
            // On affiche directement notre composable BottomNavigationRoot()
            BottomNavigationRoot()
        }
    }
}

