package com.therionlabs.capturadigital.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * DTO de entrada para crear/actualizar un prospecto desde la app móvil.
 */
data class ProspectoRequest(
    @field:NotBlank(message = "El nombre es obligatorio")
    val nombre: String,

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Email(message = "Formato de correo inválido")
    val correo: String,

    @field:NotBlank(message = "El teléfono es obligatorio")
    val telefono: String,

    @JsonProperty("nivel_educativo")
    val nivelEducativo: String = "",

    @JsonProperty("area_interes")
    val areaInteres: String = "",

    @JsonProperty("curso_interes")
    val cursoInteres: String = "",

    val modalidad: String = "",

    @JsonProperty("canal_contacto")
    val canalContacto: String = "",

    @JsonProperty("fecha_captura")
    val fechaCaptura: String = "",

    val observaciones: String = "",

    @JsonProperty("id_captador")
    val idCaptador: String = "",

    @JsonProperty("bateria_captura")
    val bateriaCaptura: Int = 0
)

/**
 * DTO de respuesta estándar.
 */
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)