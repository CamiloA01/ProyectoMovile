package com.therionlabs.capturadigital.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores corporativos THERIONLABS
val AzulOscuro = Color(0xFF1A237E)
val AzulMedio = Color(0xFF283593)
val AzulClaro = Color(0xFF3949AB)
val Naranja = Color(0xFFFF6F00)
val NaranjaClaro = Color(0xFFFF8F00)
val VerdeExito = Color(0xFF2E7D32)
val RojoError = Color(0xFFC62828)
val AzulInfo = Color(0xFF1565C0)
val GrisFondo = Color(0xFFF5F5F5)
val Blanco = Color(0xFFFFFFFF)
val GrisTexto = Color(0xFF616161)

private val LightColorScheme = lightColorScheme(
    primary = AzulOscuro,
    onPrimary = Blanco,
    primaryContainer = AzulClaro,
    onPrimaryContainer = Blanco,
    secondary = Naranja,
    onSecondary = Blanco,
    secondaryContainer = NaranjaClaro,
    background = GrisFondo,
    surface = Blanco,
    error = RojoError,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Blanco,
    primaryContainer = AzulOscuro,
    secondary = NaranjaClaro,
    onSecondary = Blanco,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFEF5350),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

@Composable
fun CapturaDigitalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}