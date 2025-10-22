package com.myapp.simpletodo.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.myapp.simpletodo.presentation.navigation.AppNavHost
import com.myapp.simpletodo.presentation.theme.SimpletodoTheme

@Composable
fun TaskApp() {
    SimpletodoTheme {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
    }
}