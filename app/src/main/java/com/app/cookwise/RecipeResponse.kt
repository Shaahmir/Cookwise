package com.app.cookwise

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("idMeal") val id: Int,
    @SerializedName("strMeal") val title: String,
    @SerializedName("strArea") val cuisine: String? = "",
    @SerializedName("strCategory") val category: String,
    @SerializedName("strInstructions") val instructions: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String,
    @SerializedName("strYoutube") val tutorialUrl: String? = ""
)

data class RecipeResponse(
    @SerializedName("meals") val recipe: List<Recipe>
)
