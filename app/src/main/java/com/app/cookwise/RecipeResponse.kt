package com.app.cookwise

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("idMeal") val id: Int,
    @SerializedName("strMeal") val title: String,
    @SerializedName("strArea") val cuisine: String? = "",
    @SerializedName("strCategory") val category: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String,
    @SerializedName("strYoutube") val tutorialUrl: String? = ""
)

data class DetailRecipe(
    @SerializedName("idMeal") val id: Int,
    @SerializedName("strMeal") val title: String,
    @SerializedName("strArea") val cuisine: String? = "",
    @SerializedName("strCategory") val category: String,
    @SerializedName("strInstructions") val instructions: String,
    @SerializedName("strMealThumb") val thumbnailUrl: String,
    @SerializedName("strYoutube") val tutorialUrl: String? = "",

    @SerializedName("strIngredient1") val ingredient1: String? = "",
    @SerializedName("strIngredient2") val ingredient2: String? = "",
    @SerializedName("strIngredient3") val ingredient3: String? = "",
    @SerializedName("strIngredient4") val ingredient4: String? = "",
    @SerializedName("strIngredient5") val ingredient5: String? = "",
    @SerializedName("strIngredient6") val ingredient6: String? = "",
    @SerializedName("strIngredient7") val ingredient7: String? = "",
    @SerializedName("strIngredient8") val ingredient8: String? = "",
    @SerializedName("strIngredient9") val ingredient9: String? = "",
    @SerializedName("strIngredient10") val ingredient10: String? = "",
    @SerializedName("strIngredient11") val ingredient11: String? = "",
    @SerializedName("strIngredient12") val ingredient12: String? = "",
    @SerializedName("strIngredient13") val ingredient13: String? = "",
    @SerializedName("strIngredient14") val ingredient14: String? = "",
    @SerializedName("strIngredient15") val ingredient15: String? = "",
    @SerializedName("strIngredient16") val ingredient16: String? = "",
    @SerializedName("strIngredient17") val ingredient17: String? = "",
    @SerializedName("strIngredient18") val ingredient18: String? = "",
    @SerializedName("strIngredient19") val ingredient19: String? = "",
    @SerializedName("strIngredient20") val ingredient20: String? = "",

    @SerializedName("strMeasure1") val strMeasure1: String? = "",
    @SerializedName("strMeasure2") val strMeasure2: String? = "",
    @SerializedName("strMeasure3") val strMeasure3: String? = "",
    @SerializedName("strMeasure4") val strMeasure4: String? = "",
    @SerializedName("strMeasure5") val strMeasure5: String? = "",
    @SerializedName("strMeasure6") val strMeasure6: String? = "",
    @SerializedName("strMeasure7") val strMeasure7: String? = "",
    @SerializedName("strMeasure8") val strMeasure8: String? = "",
    @SerializedName("strMeasure9") val strMeasure9: String? = "",
    @SerializedName("strMeasure10") val strMeasure10: String? = "",
    @SerializedName("strMeasure11") val strMeasure11: String? = "",
    @SerializedName("strMeasure12") val strMeasure12: String? = "",
    @SerializedName("strMeasure13") val strMeasure13: String? = "",
    @SerializedName("strMeasure14") val strMeasure14: String? = "",
    @SerializedName("strMeasure15") val strMeasure15: String? = "",
    @SerializedName("strMeasure16") val strMeasure16: String? = "",
    @SerializedName("strMeasure17") val strMeasure17: String? = "",
    @SerializedName("strMeasure18") val strMeasure18: String? = "",
    @SerializedName("strMeasure19") val strMeasure19: String? = "",
    @SerializedName("strMeasure20") val strMeasure20: String? = ""
) {
    val ingredients: List <String>
    get() {

        val names = listOf(
            ingredient1, ingredient2, ingredient3, ingredient4, ingredient5,
            ingredient6, ingredient7, ingredient8, ingredient9, ingredient10,
            ingredient11, ingredient12, ingredient13, ingredient14, ingredient15,
            ingredient16, ingredient17, ingredient18, ingredient19, ingredient20
        )

        val measures = listOf(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
        )

        return names.zip(measures)
            .filter { (name, measure) -> !name.isNullOrBlank() && !measure.isNullOrBlank()}
            .map { (name, measure) -> "$measure $name".trim() }
    }
}

data class RecipeResponse(
    @SerializedName("meals") val recipe: List<Recipe>
)

data class DetailResponse(
    @SerializedName("meals") val recipe: List<DetailRecipe>
)
