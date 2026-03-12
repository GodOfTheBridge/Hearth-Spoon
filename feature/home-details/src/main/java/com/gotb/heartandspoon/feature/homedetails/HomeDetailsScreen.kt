package com.gotb.heartandspoon.feature.homedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeDetailsRoute(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "\u0412\u043d\u0443\u0442\u0440\u0435\u043d\u043d\u0438\u0439 \u044d\u043a\u0440\u0430\u043d",
            style = MaterialTheme.typography.headlineMedium,
        )
        Button(onClick = onBack) {
            Text(text = "\u041d\u0430\u0437\u0430\u0434")
        }
    }
}
