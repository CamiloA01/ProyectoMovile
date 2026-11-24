package com.therionlabs.capturadigital.ui.screens.configuracion

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
import com.therionlabs.capturadigital.ui.components.HardwareInfoBlock
import com.therionlabs.capturadigital.ui.components.StatusBanner
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.*

/**
 * Pantalla de Configuración y Prueba de Red.
 * RF-MOV-020: Simulación de Modo Terreno
 * RF-MOV-021: Identificación de Hardware y Estado
 * RF-MOV-022: Diagnóstico de API REST
 */
@Composable
fun ConfiguracionScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-021: Identificación de Hardware
        HardwareInfoBlock(
            idCaptador = state.idCaptador,
            bateria = state.bateria
        )

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-020: Switch de Modo Terreno
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Terrain,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "MODO TERRENO",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (state.modoTerreno) "SIN CONEXIÓN" else "EN LÍNEA",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (state.modoTerreno) Naranja else VerdeExito
                            )
                        }
                    }
                    Switch(
                        checked = state.modoTerreno,
                        onCheckedChange = { viewModel.onModoTerrenoChange(it) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Al activar MODO TERRENO, los datos se almacenan localmente sin intentar sincronizar automáticamente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-022: Diagnóstico de API REST
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Api,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Diagnóstico de API REST",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Endpoint: http://10.0.2.2:8080/api/health",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Indicador de estado de conexión
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (state.isOnline) VerdeExito else RojoError
                    ) {
                        Text(
                            text = if (state.isOnline) "  CONECTADO  " else "  DESCONECTADO  ",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.verificarConexionAPI() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.NetworkCheck, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("VERIFICAR CONEXIÓN API", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Banner de estado
        StatusBanner(
            mensaje = state.mensajeEstado,
            tipo = state.tipoMensaje,
            onDismiss = { viewModel.limpiarMensajeEstado() }
        )
    }
}