package com.therionlabs.capturadigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.therionlabs.capturadigital.ui.navigation.AppNavigation
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.CapturaDigitalTheme

/**
 * Actividad principal de la aplicación Captura Digital.
 * Inicializa el tema, el ViewModel y la navegación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CapturaDigitalTheme {
                val viewModel: MainViewModel = viewModel()
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}