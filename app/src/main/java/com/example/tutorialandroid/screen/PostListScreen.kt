package com.example.tutorialandroid.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tutorialandroid.R

@Composable
fun PostListScreen() {
    Text(stringResource(id = R.string.welcome_post))
}