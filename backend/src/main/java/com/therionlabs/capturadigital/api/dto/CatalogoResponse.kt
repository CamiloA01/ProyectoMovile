package com.therionlabs.capturadigital.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * DTO de respuesta para el catálogo académico completo.
 */
data class CatalogoResponse(
    @JsonProperty("niveles_educativos")
    val nivelesEducativos: List<CatalogoItem> = emptyList(),

    val areas: List<AreaConCursos> = emptyList(),

    val modalidades: List<String> = listOf("Presencial", "Online"),

    @JsonProperty("canales_contacto")
    val canalesContacto: List<String> = listOf("Email", "Teléfono", "WhatsApp")
)

data class CatalogoItem(
    val id: Long,
    val nombre: String
)

data class AreaConCursos(
    val id: Long,
    val nombre: String,
    val cursos: List<CatalogoItem> = emptyList()
)