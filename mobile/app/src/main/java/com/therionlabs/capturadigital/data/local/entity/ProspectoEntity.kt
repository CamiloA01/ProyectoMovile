package com.therionlabs.capturadigital.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para almacenamiento local offline de prospectos.
 */
@Entity(tableName = "prospectos")
data class ProspectoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val correo: String,
    val telefono: String,
    val nivelEducativo: String,
    val areaInteres: String,
    val cursoInteres: String,
    val modalidad: String,       // "Presencial" o "Online"
    val canalContacto: String,   // Canal preferido de contacto
    val fechaCaptura: Long,      // Timestamp
    val observaciones: String = "",
    val sincronizado: Boolean = false,  // false = pendiente, true = enviado
    val idCaptador: String = "",       // ID del captador (ej. CPT-10)
    val bateriaCaptura: Int = 0        // % batería al momento de captura
)