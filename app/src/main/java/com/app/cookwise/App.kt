package com.app.cookwise

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App () {

    val viewModel: AppViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {

        composable (Screen.HomeScreen.route) {
            HomeScreen () { recipeId ->
                navController.currentBackStackEntry?.savedStateHandle?.set("ID", recipeId)
                navController.navigate(Screen.DetailScreen.route)
            }
        }

        composable (Screen.DetailScreen.route) {

            val recipeId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("ID") ?: -1
            val recipe = viewModel.detailRecipe.value.recipe

            LaunchedEffect (recipeId) {
                if (recipeId != -1) viewModel.getRecipeById(recipeId)
            }

            DetailScreen (viewModel = viewModel, recipe = recipe) {
                navController.popBackStack()
            }
        }
    }
}
