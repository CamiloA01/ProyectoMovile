package com.therionlabs.capturadigital.api.controller

import com.therionlabs.capturadigital.api.dto.ApiResponse
import com.therionlabs.capturadigital.api.dto.ProspectoRequest
import com.therionlabs.capturadigital.api.service.ProspectoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST para la gestión de prospectos.
 * Endpoints consumidos por la app móvil vía Retrofit.
 */
@RestController
@RequestMapping("/api/prospectos")
class ProspectoController(
    private val prospectoService: ProspectoService
) {

    /**
     * POST /api/prospectos — Crear un nuevo prospecto.
     */
    @PostMapping
    fun crearProspecto(
        @Valid @RequestBody request: ProspectoRequest
    ): ResponseEntity<ApiResponse> {
        try {
            val prospecto = prospectoService.crearProspecto(request)
            return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse(
                    success = true,
                    message = "Prospecto creado exitosamente",
                    data = prospecto
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse(
                    success = false,
                    message = "Error al crear prospecto: ${e.localizedMessage}"
                )
            )
        }
    }

    /**
     * GET /api/prospectos — Listar todos los prospectos.
     */
    @GetMapping
    fun listarProspectos(): ResponseEntity<ApiResponse> {
        val prospectos = prospectoService.obtenerTodos()
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Lista de prospectos",
                data = prospectos
            )
        )
    }

    /**
     * GET /api/prospectos/{id} — Obtener un prospecto por ID.
     */
    @GetMapping("/{id}")
    fun obtenerProspecto(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse> {
        val prospecto = prospectoService.obtenerPorId(id)
        return if (prospecto != null) {
            ResponseEntity.ok(
                ApiResponse(success = true, message = "Prospecto encontrado", data = prospecto)
            )
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse(success = false, message = "Prospecto no encontrado")
            )
        }
    }
}