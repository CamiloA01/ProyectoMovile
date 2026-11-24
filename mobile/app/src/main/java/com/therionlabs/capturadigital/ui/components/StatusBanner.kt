package com.therionlabs.capturadigital.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.*

/**
 * Banner informativo para mensajes de estado del sistema.
 * RF-MOV-019: Retroalimentación Visual de Red
 * RF-MOV-010: Confirmación de Almacenamiento Local
 */
@Composable
fun StatusBanner(
    mensaje: String?,
    tipo: MainViewModel.TipoMensaje,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = mensaje != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        mensaje?.let { msg ->
            val (backgroundColor, icon) = when (tipo) {
                MainViewModel.TipoMensaje.EXITO -> VerdeExito to Icons.Default.CheckCircle
                MainViewModel.TipoMensaje.ERROR -> RojoError to Icons.Default.Error
                MainViewModel.TipoMensaje.INFO -> AzulInfo to Icons.Default.Info
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = msg,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Indicador de estado de red con código de colores.
 * RF-MOV-019: Retroalimentación Visual de Red
 */
@Composable
fun NetworkStatusIndicator(isOnline: Boolean, modoTerreno: Boolean) {
    val (color, text) = when {
        modoTerreno -> Color(0xFF795548) to "MODO TERRENO ACTIVADO"
        isOnline -> VerdeExito to "EXCELENTE SEÑAL"
        else -> RojoError to "SIN CONEXIÓN"
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Información de hardware y captador.
 * RF-MOV-021: Identificación de Hardware y Estado
 */
@Composable
fun HardwareInfoBlock(idCaptador: String, bateria: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "ID: $idCaptador",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.BatteryFull,
                contentDescription = null,
                tint = if (bateria > 20) VerdeExito else RojoError
            )
            Text(
                text = "$bateria%",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}