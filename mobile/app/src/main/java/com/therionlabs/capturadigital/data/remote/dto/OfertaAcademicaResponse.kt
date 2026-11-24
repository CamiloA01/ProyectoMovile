package com.therionlabs.capturadigital.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para el catálogo de oferta académica desde la API.
 */
data class OfertaAcademicaResponse(
    @SerializedName("areas")
    val areas: List<AreaDTO> = emptyList()
)

data class AreaDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("cursos")
    val cursos: List<CursoDTO> = emptyList()
)

data class CursoDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String = ""
)

data class NivelEducativoDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nombre")
    val nombre: String
)

data class CatalogosResponse(
    @SerializedName("niveles_educativos")
    val nivelesEducativos: List<NivelEducativoDTO> = emptyList(),
    @SerializedName("areas")
    val areas: List<AreaDTO> = emptyList(),
    @SerializedName("modalidades")
    val modalidades: List<String> = listOf("Presencial", "Online"),
    @SerializedName("canales_contacto")
    val canalesContacto: List<String> = listOf("Email", "Teléfono", "WhatsApp")
)