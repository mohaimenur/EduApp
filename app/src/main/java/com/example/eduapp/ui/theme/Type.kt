package com.example.eduapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ArialFontFamily = FontFamily.SansSerif // Arial is typically represented by SansSerif in Android/Compose

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = ArialFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
