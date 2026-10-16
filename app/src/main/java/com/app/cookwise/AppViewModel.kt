package com.app.cookwise

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

data class RecipeState(
    val recipes: List<Recipe>? = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)

class AppViewModel : ViewModel() {
    private val _recipes = mutableStateOf(RecipeState())
    val recipe: State<RecipeState> = _recipes

    fun fetchRecipes() {
        viewModelScope.launch {
            try {
                val meals = (1..20).map {
                    async { retrofitService.getRecipe().recipe.first() }
                }.awaitAll()

                _recipes.value = _recipes.value.copy (
                    recipes = meals,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _recipes.value = _recipes.value.copy (
                    loading = false,
                    error = "ERROR: ${e.message}"
                )
            }
        }
    }

    fun searchRecipes(search: String) {
        viewModelScope.launch {
            try {
                val meals = retrofitService.searchRecipe(search).recipe
                _recipes.value = _recipes.value.copy (
                    recipes = meals,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _recipes.value = _recipes.value.copy (
                    loading = false,
                    error = "ERROR: ${e.message}"
                )
            }
        }
    }
}
