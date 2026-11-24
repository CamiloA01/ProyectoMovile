package com.therionlabs.capturadigital.api.model

import jakarta.persistence.*
import java.time.Instant

/**
 * Entidad JPA para almacenar prospectos en la base de datos central.
 */
@Entity
@Table(name = "prospectos")
data class Prospecto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nombre: String = "",

    @Column(nullable = false)
    val correo: String = "",

    @Column(nullable = false)
    val telefono: String = "",

    @Column(name = "nivel_educativo")
    val nivelEducativo: String = "",

    @Column(name = "area_interes")
    val areaInteres: String = "",

    @Column(name = "curso_interes")
    val cursoInteres: String = "",

    val modalidad: String = "",

    @Column(name = "canal_contacto")
    val canalContacto: String = "",

    @Column(name = "fecha_captura")
    val fechaCaptura: Instant = Instant.now(),

    val observaciones: String = "",

    @Column(name = "id_captador")
    val idCaptador: String = "",

    @Column(name = "bateria_captura")
    val bateriaCaptura: Int = 0,

    @Column(name = "fecha_sincronizacion")
    val fechaSincronizacion: Instant = Instant.now()
)