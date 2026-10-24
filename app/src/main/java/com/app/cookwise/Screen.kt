package com.app.cookwise

sealed class Screen (val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object DetailScreen : Screen("DetailScreen")
}
