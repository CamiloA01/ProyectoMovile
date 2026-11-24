package com.therionlabs.capturadigital.ui.screens.panel

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
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import com.therionlabs.capturadigital.ui.screens.MainViewModel
import com.therionlabs.capturadigital.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Panel de Registros con filtros y listado.
 * RF-MOV-013: Filtro de Registros Pendientes
 * RF-MOV-014: Filtro de Registros Sincronizados
 * RF-MOV-015: Actualización Manual
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()

    // Seleccionar el flow según el filtro activo
    val prospectos = when (state.filtroActivo) {
        MainViewModel.FiltroRegistro.PENDIENTES -> viewModel.pendientes
        MainViewModel.FiltroRegistro.SINCRONIZADOS -> viewModel.sincronizados
        MainViewModel.FiltroRegistro.TODOS -> viewModel.allProspectos
    }

    // Recolectar datos del flow
    val listaProspectos by prospectos.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Título y contadores
        Text(
            text = "Registros",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Total: ${state.totalProspectos}  |  Pendientes: ${state.pendientesCount}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-013/014: Pestañas de filtro
        TabRow(
            selectedTabIndex = state.filtroActivo.ordinal
        ) {
            MainViewModel.FiltroRegistro.entries.forEachIndexed { index, filtro ->
                Tab(
                    selected = state.filtroActivo == filtro,
                    onClick = { viewModel.onFiltroChange(filtro) },
                    text = {
                        when (filtro) {
                            MainViewModel.FiltroRegistro.TODOS -> Text("Todos")
                            MainViewModel.FiltroRegistro.PENDIENTES -> Text("Pendientes")
                            MainViewModel.FiltroRegistro.SINCRONIZADOS -> Text("Sincronizados")
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-015: Botón de actualización manual
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { /* La recarga es automática vía Flow */ },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Actualizar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de prospectos
        if (listaProspectos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No hay registros",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaProspectos) { prospecto ->
                    ProspectoCard(prospecto = prospecto)
                }
            }
        }
    }
}

/**
 * Tarjeta individual de prospecto en el listado.
 */
@Composable
private fun ProspectoCard(prospecto: ProspectoEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prospecto.sincronizado)
                VerdeExito.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = if (prospecto.sincronizado) VerdeExito else Naranja
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prospecto.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${prospecto.correo} · ${prospecto.telefono}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (prospecto.cursoInteres.isNotEmpty()) {
                    Text(
                        text = "${prospecto.areaInteres} → ${prospecto.cursoInteres}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Indicador de estado
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (prospecto.sincronizado) VerdeExito else Naranja.copy(alpha = 0.8f)
            ) {
                Text(
                    text = if (prospecto.sincronizado) "SINCRONIZADO" else "PENDIENTE",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}