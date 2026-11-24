package com.therionlabs.capturadigital.ui.screens.estado

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.therionlabs.capturadigital.ui.components.NetworkStatusIndicator
import com.therionlabs.capturadigital.ui.components.StatusBanner
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.*

/**
 * Pantalla de Estado (vista principal de resumen).
 * Muestra el resumen de registros y acceso a panel y sincronización.
 * RF-MOV-013, 014, 015, 016, 017, 018
 */
@Composable
fun EstadoScreen(
    viewModel: MainViewModel,
    onNavigateToPanel: () -> Unit,
    onNavigateToSync: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // RF-MOV-013/014: Indicadores de estado
        NetworkStatusIndicator(isOnline = state.isOnline, modoTerreno = state.modoTerreno)

        Spacer(modifier = Modifier.height(8.dp))

        StatusBanner(
            mensaje = state.mensajeEstado,
            tipo = state.tipoMensaje,
            onDismiss = { viewModel.limpiarMensajeEstado() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas de resumen
        Text(
            text = "Resumen de Capturas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjeta de total
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${state.totalProspectos}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Tarjeta de pendientes
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = NaranjaClaro.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SyncProblem,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Naranja
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${state.pendientesCount}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Naranja
                    )
                    Text(
                        text = "Pendientes",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Acciones principales
        Text(
            text = "Acciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-015: Actualización manual + navegación a panel
        Button(
            onClick = onNavigateToPanel,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("VER REGISTROS", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-016/017/018: Sincronización
        Button(
            onClick = onNavigateToSync,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Naranja)
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("SINCRONIZAR", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Estado del sistema
        Text(
            text = "Estado del Sistema",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("ID Captador", state.idCaptador)
                InfoRow("Batería", "${state.bateria}%")
                InfoRow("Conexión", if (state.isOnline) "En línea" else "Sin conexión")
                InfoRow("Modo Terreno", if (state.modoTerreno) "Activado" else "Desactivado")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}