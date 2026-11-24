package com.therionlabs.capturadigital.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 * Controlador de salud/health check.
 * RF-MOV-022: Usado por la app móvil para verificar conectividad con la API.
 */
@RestController
class HealthController {

    @GetMapping("/api/health")
    fun healthCheck(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "status" to "UP",
                "service" to "Captura Digital API",
                "version" to "1.0.0",
                "timestamp" to Instant.now().toString()
            )
        )
    }
}