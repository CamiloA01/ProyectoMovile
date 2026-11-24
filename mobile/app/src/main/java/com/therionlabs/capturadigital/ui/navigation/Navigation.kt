package com.therionlabs.capturadigital.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.screens.configuracion.ConfiguracionScreen
import com.therionlabs.capturadigital.ui.screens.estado.EstadoScreen
import com.therionlabs.capturadigital.ui.screens.panel.PanelScreen
import com.therionlabs.capturadigital.ui.screens.registro.RegistroScreen
import com.therionlabs.capturadigital.ui.screens.sincronizacion.SincronizacionScreen

/**
 * Navegación principal con Bottom Navigation Bar.
 * Pantallas: REGISTRO, ESTADO (Panel), CONFIGURACIÓN
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Registro : BottomNavItem("registro", "REGISTRO", Icons.Default.PersonAdd)
    data object Estado : BottomNavItem("estado", "ESTADO", Icons.Default.CheckCircle)
    data object Config : BottomNavItem("config", "CONFIG", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Registro,
        BottomNavItem.Estado,
        BottomNavItem.Config
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Captura Digital - THERIONLABS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        content = { padding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Registro.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(BottomNavItem.Registro.route) {
                    RegistroScreen(viewModel = viewModel)
                }
                composable(BottomNavItem.Estado.route) {
                    EstadoScreen(
                        viewModel = viewModel,
                        onNavigateToPanel = {
                            navController.navigate("panel")
                        },
                        onNavigateToSync = {
                            navController.navigate("sincronizacion")
                        }
                    )
                }
                composable(BottomNavItem.Config.route) {
                    ConfiguracionScreen(viewModel = viewModel)
                }
                composable("panel") {
                    PanelScreen(viewModel = viewModel)
                }
                composable("sincronizacion") {
                    SincronizacionScreen(viewModel = viewModel)
                }
            }
        }
    )
}