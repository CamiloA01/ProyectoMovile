package com.therionlabs.capturadigital.ui.screens.sincronizacion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.therionlabs.capturadigital.data.local.entity.SyncLogEntity
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla de Detalle de Sincronización.
 * RF-MOV-016: Monitoreo de Transferencia — Barra de progreso
 * RF-MOV-017: Historial de Eventos (Sync Log) — Bitácora cronológica
 * RF-MOV-018: Forzado de Envíos — Botón FORZAR ENVÍO
 */
@Composable
fun SincronizacionScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val syncLogs by viewModel.allSyncLogs.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sincronización",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Estado de red
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (state.isOnline) VerdeExito.copy(alpha = 0.1f)
                else RojoError.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (state.isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = if (state.isOnline) VerdeExito else RojoError
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (state.isOnline) "Conectado al servidor" else "Sin conexión",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-016: Barra de progreso
        if (state.syncEnProgreso) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sincronizando...", style = MaterialTheme.typography.bodyMedium)
                        Text("${state.syncProgreso}%", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = state.syncProgreso / 100f,
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Resultado de sincronización
        state.syncResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (result.fallidos == 0) VerdeExito.copy(alpha = 0.1f)
                    else Naranja.copy(alpha = 0.1f)
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (result.fallidos == 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (result.fallidos == 0) VerdeExito else Naranja
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${result.exitosos} sincronizados, ${result.fallidos} fallidos",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // RF-MOV-018: Botón FORZAR ENVÍO
        Button(
            onClick = { viewModel.sincronizarAhora() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.syncEnProgreso && state.pendientesCount > 0,
            colors = ButtonDefaults.buttonColors(containerColor = Naranja)
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "FORZAR ENVÍO (${state.pendientesCount} pendientes)",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-017: Historial de eventos (Sync Log)
        Text(
            text = "Historial de Sincronización",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (syncLogs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Sin eventos de sincronización",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(syncLogs) { log ->
                    SyncLogItem(log = log)
                }
            }
        }
    }
}

@Composable
private fun SyncLogItem(log: SyncLogEntity) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss dd/MM", Locale.getDefault()) }
    val icon = when {
        log.evento.contains("exitoso", ignoreCase = true) || log.evento.contains("completada", ignoreCase = true) -> Icons.Default.CheckCircle
        log.evento.contains("Error", ignoreCase = true) || log.evento.contains("error", ignoreCase = true) -> Icons.Default.Error
        log.evento.contains("Conexión", ignoreCase = true) -> Icons.Default.Wifi
        log.evento.contains("Guardado", ignoreCase = true) -> Icons.Default.Save
        else -> Icons.Default.Info
    }
    val color = when {
        log.evento.contains("exitoso", ignoreCase = true) || log.evento.contains("completada", ignoreCase = true) -> VerdeExito
        log.evento.contains("Error", ignoreCase = true) || log.evento.contains("error", ignoreCase = true) -> RojoError
        log.evento.contains("Conexión", ignoreCase = true) -> AzulInfo
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.evento,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                if (log.detalle.isNotEmpty()) {
                    Text(
                        text = log.detalle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = dateFormat.format(Date(log.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}