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

data class DetailState(
    val recipe: DetailRecipe? = null,
    val loading: Boolean = true,
    val error: String? = null
)

class AppViewModel : ViewModel() {
    private val _recipes = mutableStateOf(RecipeState())
    val recipe: State<RecipeState> = _recipes

    private val _detailRecipe = mutableStateOf(DetailState())
    val detailRecipe: State<DetailState> = _detailRecipe

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

    fun searchRecipe(search: String) {
        viewModelScope.launch {
            try {
                val meal = retrofitService.searchRecipe(search).recipe
                _recipes.value = _recipes.value.copy (
                    recipes = meal,
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

    fun getRecipeById(id: Int) {

        _detailRecipe.value = _detailRecipe.value.copy(
            loading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val meal = retrofitService.getRecipeById(id).recipe.first()
                _detailRecipe.value = _detailRecipe.value.copy(
                    recipe = meal,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _detailRecipe.value = _detailRecipe.value.copy(
                    loading = false,
                    error = "ERROR: ${e.message}"
                )
            }
        }
    }
}
