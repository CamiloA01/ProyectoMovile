package com.therionlabs.capturadigital.data.repository

import com.therionlabs.capturadigital.data.local.dao.ProspectoDao
import com.therionlabs.capturadigital.data.local.dao.SyncLogDao
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import com.therionlabs.capturadigital.data.local.entity.SyncLogEntity
import com.therionlabs.capturadigital.data.remote.api.RetrofitClient
import com.therionlabs.capturadigital.data.remote.dto.ProspectoRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repositorio que maneja la lógica de persistencia local y sincronización remota.
 */
class ProspectoRepository(
    private val prospectoDao: ProspectoDao,
    private val syncLogDao: SyncLogDao
) {

    private val api = RetrofitClient.apiService
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    /** Flow de todos los prospectos */
    val allProspectos: Flow<List<ProspectoEntity>> = prospectoDao.getAllProspectos()

    /** Flow de prospectos pendientes */
    val pendientes: Flow<List<ProspectoEntity>> = prospectoDao.getPendientes()

    /** Flow de prospectos sincronizados */
    val sincronizados: Flow<List<ProspectoEntity>> = prospectoDao.getSincronizados()

    /** Conteo total */
    val countAll: Flow<Int> = prospectoDao.countAll()

    /** Conteo pendientes */
    val countPendientes: Flow<Int> = prospectoDao.countPendientes()

    /** Flow de logs de sincronización */
    val allSyncLogs: Flow<List<SyncLogEntity>> = syncLogDao.getAllLogs()

    /**
     * Guarda un prospecto localmente en Room.
     */
    suspend fun guardarLocal(prospecto: ProspectoEntity): Long {
        val id = prospectoDao.insert(prospecto)
        syncLogDao.insert(
            SyncLogEntity(
                prospectoId = id,
                evento = "Guardado local",
                timestamp = System.currentTimeMillis(),
                detalle = "Prospecto ${prospecto.nombre} almacenado en dispositivo"
            )
        )
        return id
    }

    /**
     * Sincroniza un prospecto pendiente con la API REST.
     */
    suspend fun sincronizarProspecto(prospecto: ProspectoEntity): Result<Unit> {
        return try {
            val request = ProspectoRequest(
                nombre = prospecto.nombre,
                correo = prospecto.correo,
                telefono = prospecto.telefono,
                nivelEducativo = prospecto.nivelEducativo,
                areaInteres = prospecto.areaInteres,
                cursoInteres = prospecto.cursoInteres,
                modalidad = prospecto.modalidad,
                canalContacto = prospecto.canalContacto,
                fechaCaptura = dateFormat.format(Date(prospecto.fechaCaptura)),
                observaciones = prospecto.observaciones,
                idCaptador = prospecto.idCaptador,
                bateriaCaptura = prospecto.bateriaCaptura
            )

            val response = api.enviarProspecto(request)
            if (response.isSuccessful) {
                prospectoDao.marcarSincronizado(prospecto.id)
                syncLogDao.insert(
                    SyncLogEntity(
                        prospectoId = prospecto.id,
                        evento = "Envío exitoso",
                        timestamp = System.currentTimeMillis(),
                        detalle = "Prospecto ${prospecto.nombre} sincronizado con el servidor"
                    )
                )
                Result.success(Unit)
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                syncLogDao.insert(
                    SyncLogEntity(
                        prospectoId = prospecto.id,
                        evento = "Error de envío",
                        timestamp = System.currentTimeMillis(),
                        detalle = errorMsg
                    )
                )
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            syncLogDao.insert(
                SyncLogEntity(
                    prospectoId = prospecto.id,
                    evento = "Error de red",
                    timestamp = System.currentTimeMillis(),
                    detalle = "Sin conexión: ${e.localizedMessage}"
                )
            )
            Result.failure(e)
        } catch (e: Exception) {
            syncLogDao.insert(
                SyncLogEntity(
                    prospectoId = prospecto.id,
                    evento = "Error inesperado",
                    timestamp = System.currentTimeMillis(),
                    detalle = e.localizedMessage ?: "Error desconocido"
                )
            )
            Result.failure(e)
        }
    }

    /**
     * Sincroniza todos los prospectos pendientes.
     */
    suspend fun sincronizarTodos(): SyncResult {
        val pendientesList = prospectoDao.getPendientesList()
        var exitosos = 0
        var fallidos = 0

        syncLogDao.insert(
            SyncLogEntity(
                prospectoId = 0,
                evento = "Conexión detectada",
                timestamp = System.currentTimeMillis(),
                detalle = "Iniciando sincronización de ${pendientesList.size} prospectos"
            )
        )

        for (prospecto in pendientesList) {
            val result = sincronizarProspecto(prospecto)
            if (result.isSuccess) exitosos++ else fallidos++
        }

        syncLogDao.insert(
            SyncLogEntity(
                prospectoId = 0,
                evento = "Sincronización completada",
                timestamp = System.currentTimeMillis(),
                detalle = "$exitosos sincronizados, $fallidos fallidos"
            )
        )

        return SyncResult(exitosos, fallidos)
    }

    /**
     * Verifica conectividad con la API.
     */
    suspend fun verificarConexion(): Boolean {
        return try {
            val response = api.healthCheck()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtiene logs de sincronización de un prospecto.
     */
    fun getSyncLogs(prospectoId: Long): Flow<List<SyncLogEntity>> {
        return syncLogDao.getLogsForProspecto(prospectoId)
    }

    /**
     * Obtiene un prospecto por ID.
     */
    suspend fun getProspectoById(id: Long): ProspectoEntity? {
        return prospectoDao.getById(id)
    }
}

data class SyncResult(
    val exitosos: Int,
    val fallidos: Int
)