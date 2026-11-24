package com.therionlabs.capturadigital.api.model

import jakarta.persistence.*

/**
 * Entidad JPA para el catálogo académico.
 * Áreas de especialización con sus cursos asociados.
 */
@Entity
@Table(name = "areas")
data class Area(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String = ""
)

@Entity
@Table(name = "cursos")
data class Curso(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String = "",
    val descripcion: String = "",
    @Column(name = "area_id")
    val areaId: Long = 0
)

@Entity
@Table(name = "niveles_educativos")
data class NivelEducativo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nombre: String = ""
)