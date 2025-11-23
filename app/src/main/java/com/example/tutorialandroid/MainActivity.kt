package com.example.tutorialandroid

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import com.example.tutorialandroid.domain.NetworkPostRepository
import com.example.tutorialandroid.navigation.MainScaffold
import com.example.tutorialandroid.network.NetworkPost
import com.example.tutorialandroid.viewModel.PostsViewModel
import com.example.tutorialandroid.viewModel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("MesPreferences", Context.MODE_PRIVATE)
        val savedUrl = sharedPref.getString("base_url", null)
        var baseUrl = if (savedUrl.isNullOrEmpty()) "https://jsonplaceholder.typicode.com/" else savedUrl

        val networkPost = NetworkPost(baseUrl)
        val repository = NetworkPostRepository(networkPost.api)
        val factory = PostsViewModelFactory(repository)

        val viewModel: PostsViewModel by viewModels { factory }

        // lance Jetpack Compose
        setContent {
            MainScaffold(vm = viewModel)
        }
    }
}

