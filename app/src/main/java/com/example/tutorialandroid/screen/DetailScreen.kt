package com.example.tutorialandroid.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tutorialandroid.R

@Composable
fun DetailScreen() {
    Text(text = stringResource(id = R.string.detail_title))
}