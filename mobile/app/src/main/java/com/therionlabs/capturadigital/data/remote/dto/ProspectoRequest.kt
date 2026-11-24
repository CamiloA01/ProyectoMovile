package com.therionlabs.capturadigital.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para enviar un prospecto a la API REST.
 */
data class ProspectoRequest(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("telefono")
    val telefono: String,
    @SerializedName("nivel_educativo")
    val nivelEducativo: String,
    @SerializedName("area_interes")
    val areaInteres: String,
    @SerializedName("curso_interes")
    val cursoInteres: String,
    @SerializedName("modalidad")
    val modalidad: String,
    @SerializedName("canal_contacto")
    val canalContacto: String,
    @SerializedName("fecha_captura")
    val fechaCaptura: String,
    @SerializedName("observaciones")
    val observaciones: String = "",
    @SerializedName("id_captador")
    val idCaptador: String = "",
    @SerializedName("bateria_captura")
    val bateriaCaptura: Int = 0
)