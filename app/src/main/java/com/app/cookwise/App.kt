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

val adria = FontFamily(
    Font (R.font.adria_regular, FontWeight.Bold)
)

val bgLight = 0xFFF7F5F0
val fgLight = 0xFF1C1B19

val bgDark = 0xFF161513
val fgDark = 0xFFEDEAE3

@Composable
fun App () {
    val viewModel: AppViewModel = viewModel()
    val viewState by viewModel.recipe

    LaunchedEffect (Unit) {
        viewModel.fetchRecipes()
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
                    viewModel.searchRecipes(it)
                }, {
                    viewModel.fetchRecipes()
                })
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>?, onSearch: (String) -> Unit, onClear: () -> Unit) {

    var searchInput by remember { mutableStateOf("") }

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
                    color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
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
                    modifier = Modifier.padding(start = 10.dp,  bottom = 8.dp, end = 10.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(100),
                    singleLine = true,
                    trailingIcon = {
                        Button (
                            shape = RoundedCornerShape(100),
                            contentPadding = PaddingValues( horizontal = 24.dp),
                            modifier = Modifier.padding(end = 5.dp).height(50.dp),
                            onClick = {onSearch(searchInput.trim())},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSystemInDarkTheme()) Color(fgLight) else Color(fgDark),
                                contentColor = if (isSystemInDarkTheme()) Color(bgLight) else Color(bgDark)
                            )
                        ) {
                            Text("Search")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                        unfocusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                        focusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        focusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        unfocusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        cursorColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )
            }
        }

        if (recipes.isNullOrEmpty()) {
            item {
                NoRecipes()
            }
        } else {
            items (recipes) { recipes ->
                RecipeCard(
                    recipes = recipes
                )
            }
        }
    }
}

@Composable
fun RecipeCard(recipes: Recipe) {

    val uriHandler = LocalUriHandler.current
    val videoUrl = getUrl(recipes)
    var showInfo by remember {mutableStateOf(false)}

    Column (
        modifier = Modifier.padding(16.dp).fillMaxSize().clickable(
            onClick = {showInfo = true}
        )
    ) {
        Image (
            painter = rememberAsyncImagePainter(recipes.thumbnailUrl),
            contentDescription = "Meal Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
        )

        Text (
            text = recipes.title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 16.dp, start = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text (
            text = recipes.category,
            color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

        Text (
            text = recipes.cuisine ?: "Multi-Cuisine",
            color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

        Button (
            onClick = {uriHandler.openUri(videoUrl)},
            modifier = Modifier.padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                contentColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
            )
        ) {
            Text("Tutorial \u21AA")
        }

        if (showInfo) {
            RecipeInfo(recipes = recipes){
                showInfo = false
            }
        }
    }
}

fun getUrl(recipes: Recipe): String {
    if (recipes.tutorialUrl.isNullOrBlank()) {
        return "https://www.youtube.com/results?search_query=${recipes.title.lowercase().replace(" ", "+")}"
    }
    return recipes.tutorialUrl
}

@Composable
fun NoRecipes() {
    Box (
        modifier = Modifier.fillMaxSize().padding(top = 150.dp),
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
                text = "No recipes found!"
            )
        }
    }
}

@Composable
fun RecipeInfo(recipes: Recipe, showInfo: () -> Unit) {
    Dialog(onDismissRequest = { showInfo() }) {
        Surface (
            shape = RoundedCornerShape(28.dp),
            color = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight)
        ){
            Column (modifier = Modifier.padding(24.dp)) {
                Text(
                    text = recipes.title,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column (
                    modifier = Modifier.heightIn(max = 250.dp).verticalScroll(rememberScrollState()),) {
                    Text(
                        text = recipes.instructions,
                        color = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    onClick = { showInfo() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "OK",
                        color = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                    )
                }
            }
        }
    }
}
