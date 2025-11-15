package com.example.tutorialandroid.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tutorialandroid.R
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onNavigateToDetail: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // marge intérieure
        verticalArrangement = Arrangement.Center, // centre verticalement
        horizontalAlignment = Alignment.CenterHorizontally // centre horizontalement
    ) {
        Text(
            text = stringResource(id = R.string.welcome_home)
        )
        Spacer(modifier = Modifier.height(24.dp)) // espace entre les éléments
        Button(onClick = onNavigateToDetail) {
            Text(text = stringResource(id = R.string.home_button_detail))
        }
    }
}
