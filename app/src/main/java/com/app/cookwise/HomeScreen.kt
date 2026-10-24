package com.app.cookwise

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import kotlin.collections.getValue
import kotlin.collections.setValue
import kotlin.getValue
import kotlin.setValue

val adria = FontFamily(
    Font (R.font.adria_regular, FontWeight.Bold)
)
val bgLight = 0xFFF7F5F0
val fgLight = 0xFF1C1B19

val bgDark = 0xFF161513
val fgDark = 0xFFEDEAE3

@Composable
fun HomeScreen (DetailNavigation: (id: Int) -> Unit) {

    val viewModel: AppViewModel = viewModel()
    val viewState by viewModel.recipe

    LaunchedEffect (Unit) {
        if (viewState.recipes.isNullOrEmpty()) viewModel.fetchRecipes()
    }

    Box (modifier = Modifier.statusBarsPadding()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align (Alignment.Center))
            }

            viewState.error != null -> {
                Text("An unexpected error occurred!")
            }

            else -> {
                RecipeList(recipes = viewState.recipes, {
                    viewModel.searchRecipe(it)
                }, {
                    viewModel.fetchRecipes()
                }, { id ->
                    DetailNavigation(id)
                })
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>?, onSearch: (String) -> Unit, onClear: () -> Unit, onClick: (id: Int) -> Unit) {

    var searchInput by remember { mutableStateOf("") }
    val isDark = isSystemInDarkTheme()

    LazyVerticalGrid (
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize()
    ) {
        item () {
            Column (modifier = Modifier.fillMaxSize()) {
                Text (
                    text = "Cookwise",
                    style = TextStyle(
                        fontFamily = adria,
                        fontWeight = FontWeight.Bold,
                        fontSize = 52.sp
                    ),
                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp, start = 14.dp),
                )

                Text (
                    text = "Can't decide what to cook? Gotcha!",
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp,  bottom = 16.dp)
                )

                OutlinedTextField (
                    value = searchInput,
                    onValueChange = {
                        searchInput = it
                        if (it.isBlank()) { onClear() }
                    },
                    placeholder = { Text("Search for a recipe") },
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 8.dp, end = 10.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(100),
                    singleLine = true,
                    trailingIcon = {
                        Button (
                            shape = RoundedCornerShape(100),
                            contentPadding = PaddingValues( horizontal = 24.dp),
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .height(50.dp),
                            onClick = {onSearch(searchInput.trim())},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) Color(fgLight) else Color(fgDark),
                                contentColor = if (isDark) Color(bgLight) else Color(bgDark)
                            )
                        ) {
                            Text("Search")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) Color(fgDark) else Color(fgLight),
                        unfocusedContainerColor = if (isDark) Color(fgDark) else Color(fgLight),
                        focusedTextColor = if (isDark) Color(bgDark) else Color(bgLight),
                        unfocusedTextColor = if (isDark) Color(bgDark) else Color(bgLight),
                        focusedBorderColor = if (isDark) Color(bgDark) else Color(bgLight),
                        unfocusedBorderColor = if (isDark) Color(bgDark) else Color(bgLight),
                        cursorColor = if (isDark) Color(bgDark) else Color(bgLight),
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )
            }
        }

        if (recipes.isNullOrEmpty()) {
            item {
                NoRecipe()
            }
        } else {
            items (recipes) { recipe ->
                RecipeCard(
                    recipe = recipe
                ) {
                    onClick(recipe.id)
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: (id: Int) -> Unit) {

    val uriHandler = LocalUriHandler.current
    val videoUrl = getUrl(recipe.tutorialUrl, recipe.title)
    val isDark = isSystemInDarkTheme()

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(
                onClick = { onClick(recipe.id) }
            )
    ) {
        Image (
            painter = rememberAsyncImagePainter(recipe.thumbnailUrl),
            contentDescription = "Meal Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
        )

        Text (
            text = recipe.title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 16.dp, start = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text (
            text = recipe.category,
            color = if (isDark) Color.LightGray else Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

        Text (
            text = recipe.cuisine ?: "Multi-Cuisine",
            color = if (isDark) Color.LightGray else Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

        Button (
            onClick = {uriHandler.openUri(videoUrl)},
            modifier = Modifier.padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDark) Color(fgDark) else Color(fgLight),
                contentColor = if (isDark) Color(bgDark) else Color(bgLight)
            )
        ) {
            Text("Tutorial \u21AA")
        }
    }
}

fun getUrl(tutorialUrl: String?, title: String): String {

    if (tutorialUrl == null) return ""

    if (tutorialUrl.isNullOrBlank()) {
        return "https://www.youtube.com/results?search_query=${title.lowercase().replace(" ", "+")}"
    }

    return tutorialUrl
}

@Composable
fun NoRecipe() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.no_search_result),
                contentDescription = "Not Found SVG",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "No recipe found!"
            )
        }
    }
}
