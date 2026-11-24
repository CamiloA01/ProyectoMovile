package com.therionlabs.capturadigital.api.service

import com.therionlabs.capturadigital.api.dto.ProspectoRequest
import com.therionlabs.capturadigital.api.model.Prospecto
import com.therionlabs.capturadigital.api.repository.ProspectoRepository
import org.springframework.stereotype.Service
import java.time.Instant

/**
 * Servicio para la gestión de prospectos.
 * Contiene la lógica de negocio antes de persistir en la base de datos.
 */
@Service
class ProspectoService(
    private val prospectoRepository: ProspectoRepository
) {

    /**
     * Guarda un nuevo prospecto recibido desde la app móvil.
     */
    fun crearProspecto(request: ProspectoRequest): Prospecto {
        val prospecto = Prospecto(
            nombre = request.nombre,
            correo = request.correo,
            telefono = request.telefono,
            nivelEducativo = request.nivelEducativo,
            areaInteres = request.areaInteres,
            cursoInteres = request.cursoInteres,
            modalidad = request.modalidad,
            canalContacto = request.canalContacto,
            fechaCaptura = Instant.now(),
            observaciones = request.observaciones,
            idCaptador = request.idCaptador,
            bateriaCaptura = request.bateriaCaptura,
            fechaSincronizacion = Instant.now()
        )
        return prospectoRepository.save(prospecto)
    }

    /**
     * Obtiene todos los prospectos registrados.
     */
    fun obtenerTodos(): List<Prospecto> {
        return prospectoRepository.findAll()
    }

    /**
     * Obtiene un prospecto por su ID.
     */
    fun obtenerPorId(id: Long): Prospecto? {
        return prospectoRepository.findById(id).orElse(null)
    }

    /**
     * Obtiene prospectos por captador.
     */
    fun obtenerPorCaptador(idCaptador: String): List<Prospecto> {
        return prospectoRepository.findByIdCaptador(idCaptador)
    }
}