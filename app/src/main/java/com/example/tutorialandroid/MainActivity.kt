package com.example.tutorialandroid

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import com.example.tutorialandroid.database.DatabaseModule
import com.example.tutorialandroid.network.Environment
import com.example.tutorialandroid.domain.NetworkPostRepository
import com.example.tutorialandroid.navigation.MainScaffold
import com.example.tutorialandroid.network.NetworkPost
import com.example.tutorialandroid.viewModel.PostsViewModel
import com.example.tutorialandroid.viewModel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Instancier la classe Environment en lui donnant le contexte courant (l'activité)
        val environment = Environment(this)

        // 2. Récupérer l'URL (proprement, sans null check inutile)
        val currentUrl = environment.getBaseUrl()

        val networkPost = NetworkPost(currentUrl)

        val db = DatabaseModule.provideDatabase(this)
        val postDao = db.postDao()

        val repository = NetworkPostRepository(api = networkPost.api, postDao = postDao)
        val factory = PostsViewModelFactory(repository)

        val viewModel: PostsViewModel by viewModels { factory }

        // lance Jetpack Compose
        setContent {
            MainScaffold(vm = viewModel)
        }
    }
}

