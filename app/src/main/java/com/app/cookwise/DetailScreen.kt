package com.app.cookwise

import android.content.ClipData
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(viewModel: AppViewModel, recipe: DetailRecipe?, HomeNavigation: () -> Unit) {

    val viewState by viewModel.detailRecipe

    Box(modifier = Modifier.statusBarsPadding()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            viewState.error != null -> {
                Text("An unexpected error occurred!")
            }

            else -> {
                DetailScreenContent(
                    recipe = recipe,
                    HomeNavigation = HomeNavigation
                )
            }
        }
    }
}

@Composable
fun DetailScreenContent(recipe: DetailRecipe?, HomeNavigation: () -> Unit) {

    if (recipe == null) {
        NoRecipe()
        return
    }

    val uriHandler = LocalUriHandler.current
    val videoUrl = getUrl(recipe.tutorialUrl, recipe.title)
    val isDark = isSystemInDarkTheme()

    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Surface () {
        Box (modifier = Modifier.fillMaxSize()) {
            Column (modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(
                rememberScrollState()
            )) {

                Text (
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .widthIn(max = 200.dp)
                        .padding(start = 5.dp, top = 10.dp, bottom = 30.dp)
                        .clickable(
                            onClick = {
                                scope.launch {
                                    val clipData = ClipData.newPlainText("title", recipe.title)
                                    clipboard.setClipEntry(clipData.toClipEntry())
                                }
                            }
                        )
                    ,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Image (
                    painter = rememberAsyncImagePainter(recipe.thumbnailUrl),
                    contentDescription = "Meal Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                )

                Text (
                    text = "Category: " + recipe.category,
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 15.dp, start = 4.dp)
                )

                Text (
                    text = "Cuisine: " + (recipe.cuisine ?: "Multi-Cuisine"),
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )

                Button (
                    onClick = { uriHandler.openUri(videoUrl) },
                    modifier = Modifier.padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(fgDark) else Color(fgLight),
                        contentColor = if (isDark) Color(bgDark) else Color(bgLight)
                    )
                ) {
                    Text("Tutorial \u21AA")
                }

                Text (
                    text = "Ingredients:",
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 4.dp)
                )

                Text (
                    text = " •    " + recipe.ingredients.joinToString ("\n •    "),
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 4.dp)
                )

                Text (
                    text = "Instructions:",
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 4.dp)
                )

                Text(
                    text = recipe.instructions ?: "Instructions are not provided for this recipe!",
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 20.dp, start = 4.dp)
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            IconButton (
                onClick = { HomeNavigation() },
                modifier = Modifier.align(Alignment.TopStart).padding(20.dp).size(46.dp).border(
                    width = 1.dp,
                    color = if (isDark) Color(0xBAFFFFFF) else Color(fgLight),
                    shape = RoundedCornerShape(100)
                ),
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isDark) Color(fgDark).copy(0.6f) else Color(fgLight),
                    contentColor = if (isDark) Color(bgDark) else Color(bgLight)
                )
            ) {
                Icon (
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = "Back Icon",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
