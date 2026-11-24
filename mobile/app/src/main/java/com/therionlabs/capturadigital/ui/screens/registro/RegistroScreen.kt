package com.therionlabs.capturadigital.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.therionlabs.capturadigital.ui.components.HardwareInfoBlock
import com.therionlabs.capturadigital.ui.components.NetworkStatusIndicator
import com.therionlabs.capturadigital.ui.components.StatusBanner
import com.therionlabs.capturadigital.ui.screens.MainViewModel

/**
 * Pantalla principal de Registro (Inicio).
 * RF-MOV-01: Acceso Rápido a Registro — Botón "REGISTRAR NUEVO PROSPECTO"
 * RF-MOV-02: Consulta de Oferta Académica — Selector desplegable
 * RF-MOV-04: Captura de Datos de Contacto — Campos Nombre, Correo, Teléfono
 * RF-MOV-05: Validación en Tiempo Real — Validación de correo
 * RF-MOV-06: Guardado Local Preventivo — Botón GUARDAR REGISTRO LOCAL
 * RF-MOV-07.1: Selección de Catálogo Académico — Dropdowns
 * RF-MOV-08: Selección de Modalidad — RadioButtons
 * RF-MOV-09: Confirmación de Captura — Botón CONFIRMAR REGISTRO
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Catálogos por defecto (fallback si no hay API)
    val nivelesEducativos = listOf("Básica", "Media", "Técnico Superior", "Profesional", "Postgrado")
    val areasPorDefecto = listOf("Tecnología", "Negocios", "Salud", "Arte", "Idiomas")
    val cursosPorDefecto = mapOf(
        "Tecnología" to listOf("Programación Web", "Ciberseguridad", "Data Science", "Cloud Computing"),
        "Negocios" to listOf("Marketing Digital", "Finanzas", "Gestión de Proyectos"),
        "Salud" to listOf("Enfermería", "Nutrición", "Psicología"),
        "Arte" to listOf("Diseño Gráfico", "Fotografía", "Animación Digital"),
        "Idiomas" to listOf("Inglés", "Portugués", "Francés", "Alemán")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Indicadores de estado
        NetworkStatusIndicator(
            isOnline = state.isOnline,
            modoTerreno = state.modoTerreno
        )

        HardwareInfoBlock(
            idCaptador = state.idCaptador,
            bateria = state.bateria
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Banner de estado
        StatusBanner(
            mensaje = state.mensajeEstado,
            tipo = state.tipoMensaje,
            onDismiss = { viewModel.limpiarMensajeEstado() }
        )

        // RF-MOV-01: Botón principal de registro
        Button(
            onClick = {
                viewModel.guardarRegistroLocal()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "REGISTRAR NUEVO PROSPECTO",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-02: Selector de oferta académica
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Navegar a catálogo */ }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Oferta Académica", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RF-MOV-04: Formulario de contacto
        Text(
            text = "Datos del Prospecto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text("Nombre completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // RF-MOV-05: Validación de correo en tiempo real
        OutlinedTextField(
            value = state.correo,
            onValueChange = { viewModel.onCorreoChange(it) },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.errorCorreo != null,
            supportingText = state.errorCorreo?.let { error ->
                { Text(error, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.telefono,
            onValueChange = { viewModel.onTelefonoChange(it) },
            label = { Text("Teléfono de contacto") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // RF-MOV-07.1: Preferencias académicas
        if (state.registroGuardado) {
            Text(
                text = "Preferencias Académicas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Nivel educativo
            var nivelExpandido by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = nivelExpandido,
                onExpandedChange = { nivelExpandido = it }
            ) {
                OutlinedTextField(
                    value = state.nivelEducativo.ifEmpty { "Seleccionar nivel educativo" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nivel Educativo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = nivelExpandido) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = nivelExpandido,
                    onDismissRequest = { nivelExpandido = false }
                ) {
                    nivelesEducativos.forEach { nivel ->
                        DropdownMenuItem(
                            text = { Text(nivel) },
                            onClick = {
                                viewModel.onNivelEducativoChange(nivel)
                                nivelExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Área de interés
            var areaExpandido by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = areaExpandido,
                onExpandedChange = { areaExpandido = it }
            ) {
                OutlinedTextField(
                    value = state.areaInteres.ifEmpty { "Seleccionar área de interés" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Área de Interés") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = areaExpandido) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(
                    expanded = areaExpandido,
                    onDismissRequest = { areaExpandido = false }
                ) {
                    areasPorDefecto.forEach { area ->
                        DropdownMenuItem(
                            text = { Text(area) },
                            onClick = {
                                viewModel.onAreaInteresChange(area)
                                areaExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Curso de interés (dependiente del área)
            if (state.areaInteres.isNotEmpty()) {
                var cursoExpandido by remember { mutableStateOf(false) }
                val cursosDisponibles = cursosPorDefecto[state.areaInteres] ?: emptyList()

                ExposedDropdownMenuBox(
                    expanded = cursoExpandido,
                    onExpandedChange = { cursoExpandido = it }
                ) {
                    OutlinedTextField(
                        value = state.cursoInteres.ifEmpty { "Seleccionar curso" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso de Interés") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cursoExpandido) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = cursoExpandido,
                        onDismissRequest = { cursoExpandido = false }
                    ) {
                        cursosDisponibles.forEach { curso ->
                            DropdownMenuItem(
                                text = { Text(curso) },
                                onClick = {
                                    viewModel.onCursoInteresChange(curso)
                                    cursoExpandido = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RF-MOV-08: Modalidad (RadioButtons)
            Text(
                text = "Modalidad preferida",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = state.modalidad == "Presencial",
                        onClick = { viewModel.onModalidadChange("Presencial") }
                    )
                    Text("Presencial")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = state.modalidad == "Online",
                        onClick = { viewModel.onModalidadChange("Online") }
                    )
                    Text("Online")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RF-MOV-09: Confirmar registro
            Button(
                onClick = { viewModel.confirmarRegistroCompleto() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CONFIRMAR REGISTRO", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RF-MOV-012: Nuevo registro (después de guardado)
        if (state.registroGuardado) {
            OutlinedButton(
                onClick = { viewModel.nuevoRegistro() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("NUEVO REGISTRO")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}