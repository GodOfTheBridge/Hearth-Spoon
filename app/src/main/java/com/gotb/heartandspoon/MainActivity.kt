package com.gotb.heartandspoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gotb.heartandspoon.core.designsystem.HearthSpoonTheme
import com.gotb.heartandspoon.navigation.HearthSpoonAppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HearthSpoonTheme {
                HearthSpoonAppNavigation()
            }
        }
    }
}