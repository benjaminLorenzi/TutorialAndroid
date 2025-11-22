package com.example.tutorialandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.tutorialandroid.domain.NetworkPostRepository
import com.example.tutorialandroid.navigation.MainScaffold
import com.example.tutorialandroid.network.NetworkPost
import com.example.tutorialandroid.viewModel.PostsViewModel
import com.example.tutorialandroid.viewModel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent { ... } lance Jetpack Compose

        val baseUrl = "https://jsonplaceholder.typicode.com/"
        val networkPost = NetworkPost(baseUrl)
        val repository = NetworkPostRepository(networkPost.api)
        val factory = PostsViewModelFactory(repository)

        val viewModel: PostsViewModel by viewModels { factory }

        setContent {
            // On affiche directement notre composable BottomNavigationRoot()
            MainScaffold(vm = viewModel)
        }
    }
}

