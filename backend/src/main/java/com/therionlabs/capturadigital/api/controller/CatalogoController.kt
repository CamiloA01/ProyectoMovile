package com.therionlabs.capturadigital.api.controller

import com.therionlabs.capturadigital.api.dto.ApiResponse
import com.therionlabs.capturadigital.api.dto.AreaConCursos
import com.therionlabs.capturadigital.api.dto.CatalogoItem
import com.therionlabs.capturadigital.api.dto.CatalogoResponse
import com.therionlabs.capturadigital.api.repository.AreaRepository
import com.therionlabs.capturadigital.api.repository.CursoRepository
import com.therionlabs.capturadigital.api.repository.NivelEducativoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador REST para el catálogo académico.
 * RF-MOV-02: Consulta de Oferta Académica
 * RNF-08: Catálogo dinámico vía API REST
 */
@RestController
@RequestMapping("/api/catalogos")
class CatalogoController(
    private val areaRepository: AreaRepository,
    private val cursoRepository: CursoRepository,
    private val nivelEducativoRepository: NivelEducativoRepository
) {

    @GetMapping
    fun obtenerCatalogos(): ResponseEntity<ApiResponse> {
        val niveles = nivelEducativoRepository.findAll().map {
            CatalogoItem(id = it.id, nombre = it.nombre)
        }

        val areas = areaRepository.findAll().map { area ->
            val cursos = cursoRepository.findByAreaId(area.id).map {
                CatalogoItem(id = it.id, nombre = it.nombre)
            }
            AreaConCursos(id = area.id, nombre = area.nombre, cursos = cursos)
        }

        val response = CatalogoResponse(
            nivelesEducativos = niveles,
            areas = areas
        )

        return ResponseEntity.ok(
            ApiResponse(success = true, message = "Catálogos cargados", data = response)
        )
    }
}