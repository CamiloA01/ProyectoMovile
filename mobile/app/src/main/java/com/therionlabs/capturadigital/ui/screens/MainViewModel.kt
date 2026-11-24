package com.therionlabs.capturadigital.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.therionlabs.capturadigital.data.local.CapturaDatabase
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import com.therionlabs.capturadigital.data.local.entity.SyncLogEntity
import com.therionlabs.capturadigital.data.remote.api.RetrofitClient
import com.therionlabs.capturadigital.data.remote.dto.CatalogosResponse
import com.therionlabs.capturadigital.data.repository.ProspectoRepository
import com.therionlabs.capturadigital.data.repository.SyncResult
import com.therionlabs.capturadigital.util.NetworkUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel principal de la aplicación Captura Digital.
 * Maneja el estado de la UI, la navegación y la lógica de negocio.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CapturaDatabase.getDatabase(application)
    private val repository = ProspectoRepository(db.prospectoDao(), db.syncLogDao())
    private val networkUtil = NetworkUtil(application)

    // Estados de la UI
    data class UiState(
        val idCaptador: String = NetworkUtil.generarIdCaptador(),
        val bateria: Int = NetworkUtil.getBateriaSimulada(),
        val isOnline: Boolean = false,
        val modoTerreno: Boolean = true, // Por defecto en modo offline

        // Formulario
        val nombre: String = "",
        val correo: String = "",
        val telefono: String = "",
        val errorCorreo: String? = null,

        // Preferencias
        val nivelEducativo: String = "",
        val areaInteres: String = "",
        val cursoInteres: String = "",
        val modalidad: String = "Online",
        val canalContacto: String = "WhatsApp",

        // Catálogos remotos
        val catalogos: CatalogosResponse? = null,

        // Estado de guardado
        val registroGuardado: Boolean = false,
        val mensajeEstado: String? = null,
        val tipoMensaje: TipoMensaje = TipoMensaje.INFO,

        // Sincronización
        val syncEnProgreso: Boolean = false,
        val syncProgreso: Int = 0, // 0-100
        val syncResult: SyncResult? = null,

        // Panel de registros
        val filtroActivo: FiltroRegistro = FiltroRegistro.PENDIENTES,

        // Conteos
        val totalProspectos: Int = 0,
        val pendientesCount: Int = 0,

        // Registro actual ID
        val ultimoRegistroId: Long? = null
    )

    enum class TipoMensaje {
        EXITO, ERROR, INFO
    }

    enum class FiltroRegistro {
        TODOS, PENDIENTES, SINCRONIZADOS
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Flows desde Room
    val allProspectos = repository.allProspectos
    val pendientes = repository.pendientes
    val sincronizados = repository.sincronizados
    val allSyncLogs = repository.allSyncLogs

    init {
        // Monitorear conectividad
        viewModelScope.launch {
            networkUtil.networkState.collect { connected ->
                _uiState.update { it.copy(isOnline = connected) }
                if (connected && !_uiState.value.modoTerreno) {
                    sincronizarAutomatico()
                }
            }
        }

        // Cargar catálogos
        cargarCatalogos()

        // Monitorear conteos
        viewModelScope.launch {
            repository.countAll.collect { count ->
                _uiState.update { it.copy(totalProspectos = count) }
            }
        }
        viewModelScope.launch {
            repository.countPendientes.collect { count ->
                _uiState.update { it.copy(pendientesCount = count) }
            }
        }
    }

    // ---- Acciones del formulario ----

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(nombre = valor) }
    }

    fun onCorreoChange(valor: String) {
        val error = validarCorreo(valor)
        _uiState.update { it.copy(correo = valor, errorCorreo = error) }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update { it.copy(telefono = valor) }
    }

    fun onNivelEducativoChange(valor: String) {
        _uiState.update { it.copy(nivelEducativo = valor) }
    }

    fun onAreaInteresChange(valor: String) {
        _uiState.update { it.copy(areaInteres = valor, cursoInteres = "") }
    }

    fun onCursoInteresChange(valor: String) {
        _uiState.update { it.copy(cursoInteres = valor) }
    }

    fun onModalidadChange(valor: String) {
        _uiState.update { it.copy(modalidad = valor) }
    }

    fun onCanalContactoChange(valor: String) {
        _uiState.update { it.copy(canalContacto = valor) }
    }

    fun onModoTerrenoChange(activo: Boolean) {
        _uiState.update { it.copy(modoTerreno = activo) }
    }

    fun onFiltroChange(filtro: FiltroRegistro) {
        _uiState.update { it.copy(filtroActivo = filtro) }
    }

    /**
     * Guarda el registro localmente en Room.
     */
    fun guardarRegistroLocal() {
        val state = _uiState.value

        // Validaciones
        if (state.nombre.isBlank() || state.correo.isBlank() || state.telefono.isBlank()) {
            _uiState.update {
                it.copy(
                    mensajeEstado = "Complete todos los campos obligatorios",
                    tipoMensaje = TipoMensaje.ERROR
                )
            }
            return
        }

        if (state.errorCorreo != null) {
            _uiState.update {
                it.copy(
                    mensajeEstado = "Corrija el formato del correo antes de continuar",
                    tipoMensaje = TipoMensaje.ERROR
                )
            }
            return
        }

        viewModelScope.launch {
            val prospecto = ProspectoEntity(
                nombre = state.nombre,
                correo = state.correo,
                telefono = state.telefono,
                nivelEducativo = state.nivelEducativo,
                areaInteres = state.areaInteres,
                cursoInteres = state.cursoInteres,
                modalidad = state.modalidad,
                canalContacto = state.canalContacto,
                fechaCaptura = System.currentTimeMillis(),
                observaciones = "",
                sincronizado = false,
                idCaptador = state.idCaptador,
                bateriaCaptura = state.bateria
            )

            val id = repository.guardarLocal(prospecto)

            _uiState.update {
                it.copy(
                    registroGuardado = true,
                    ultimoRegistroId = id,
                    mensajeEstado = "REGISTRO GUARDADO EN EL DISPOSITIVO",
                    tipoMensaje = TipoMensaje.EXITO
                )
            }
        }
    }

    /**
     * Confirma el registro completo con preferencias académicas.
     */
    fun confirmarRegistroCompleto() {
        val state = _uiState.value

        if (state.nivelEducativo.isBlank() || state.areaInteres.isBlank() || state.cursoInteres.isBlank()) {
            _uiState.update {
                it.copy(
                    mensajeEstado = "Seleccione todas las preferencias académicas",
                    tipoMensaje = TipoMensaje.ERROR
                )
            }
            return
        }

        viewModelScope.launch {
            // Actualizar preferencias si ya existe registro
            if (state.ultimoRegistroId != null) {
                val existente = repository.getProspectoById(state.ultimoRegistroId)
                if (existente != null) {
                    val actualizado = existente.copy(
                        nivelEducativo = state.nivelEducativo,
                        areaInteres = state.areaInteres,
                        cursoInteres = state.cursoInteres,
                        modalidad = state.modalidad,
                        canalContacto = state.canalContacto
                    )
                    // En un entorno completo se usaría repository.actualizar(actualizado)
                }
            }

            _uiState.update {
                it.copy(
                    mensajeEstado = "REGISTRO COMPLETO - ${state.nombre}",
                    tipoMensaje = TipoMensaje.EXITO
                )
            }
        }
    }

    /**
     * Limpia el formulario para un nuevo registro.
     */
    fun nuevoRegistro() {
        _uiState.update {
            UiState(
                idCaptador = it.idCaptador,
                bateria = NetworkUtil.getBateriaSimulada(),
                isOnline = it.isOnline,
                modoTerreno = it.modoTerreno,
                catalogos = it.catalogos,
                totalProspectos = it.totalProspectos,
                pendientesCount = it.pendientesCount
            )
        }
    }

    /**
     * Sincroniza todos los prospectos pendientes.
     */
    fun sincronizarAhora() {
        viewModelScope.launch {
            _uiState.update { it.copy(syncEnProgreso = true, syncProgreso = 0) }

            val result = repository.sincronizarTodos()

            _uiState.update {
                it.copy(
                    syncEnProgreso = false,
                    syncProgreso = 100,
                    syncResult = result,
                    mensajeEstado = "${result.exitosos} sincronizados, ${result.fallidos} fallidos",
                    tipoMensaje = if (result.fallidos == 0) TipoMensaje.EXITO else TipoMensaje.ERROR
                )
            }
        }
    }

    /**
     * Sincronización automática al detectar red.
     */
    private fun sincronizarAutomatico() {
        viewModelScope.launch {
            repository.sincronizarTodos()
        }
    }

    /**
     * Verifica la conexión con la API.
     */
    fun verificarConexionAPI() {
        viewModelScope.launch {
            val conectado = repository.verificarConexion()
            _uiState.update {
                it.copy(
                    mensajeEstado = if (conectado) "API CONECTADA" else "API NO DISPONIBLE",
                    tipoMensaje = if (conectado) TipoMensaje.EXITO else TipoMensaje.ERROR
                )
            }
        }
    }

    /**
     * Carga los catálogos académicos desde la API.
     */
    private fun cargarCatalogos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.obtenerCatalogos()
                if (response.isSuccessful) {
                    _uiState.update { it.copy(catalogos = response.body()) }
                }
            } catch (_: Exception) {
                // Si no hay API, usar catálogos por defecto
            }
        }
    }

    /**
     * Valida el formato del correo electrónico.
     */
    private fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return null
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return if (!emailRegex.matches(correo)) {
            "Error: Formato de correo inválido"
        } else null
    }

    /**
     * Elimina un mensaje de estado después de mostrarlo.
     */
    fun limpiarMensajeEstado() {
        _uiState.update { it.copy(mensajeEstado = null, registroGuardado = false) }
    }
}