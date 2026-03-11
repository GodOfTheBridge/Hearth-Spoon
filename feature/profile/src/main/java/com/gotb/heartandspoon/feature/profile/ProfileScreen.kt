package com.gotb.heartandspoon.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ProfileRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    ProfileScreen(title = viewModel.title)
}

@Composable
private fun ProfileScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}