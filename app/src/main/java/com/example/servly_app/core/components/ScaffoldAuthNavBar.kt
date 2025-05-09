package com.example.servly_app.core.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ScaffoldAuthNavBar(
    navController: NavController,
    title: String? = null,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, title) }
    ) { initialPadding ->
        content(initialPadding)
    }
}