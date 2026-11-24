package com.therionlabs.capturadigital.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bitácora cronológica de eventos de sincronización.
 */
@Entity(tableName = "sync_logs")
data class SyncLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val prospectoId: Long,
    val evento: String,       // "Guardado local", "Conexión detectada", "Envío", "Actualización"
    val timestamp: Long,
    val detalle: String = ""
)