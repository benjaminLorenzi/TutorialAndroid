package com.example.tutorialandroid.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.example.tutorialandroid.R

@Composable
fun HomeScreen() {
    Text(stringResource(id = R.string.welcome_home))
}