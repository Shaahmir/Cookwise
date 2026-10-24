package com.app.cookwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.app.cookwise.ui.theme.CookwiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CookwiseTheme {
                val isDark = isSystemInDarkTheme()
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = if (isDark) Color(bgDark) else Color(bgLight),
                    contentColor = if (isDark) Color(fgDark) else Color(fgLight)
                ) {
                    App()
                }
            }
        }
    }
}
