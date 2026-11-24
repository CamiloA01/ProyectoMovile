package com.therionlabs.capturadigital.api.repository

import com.therionlabs.capturadigital.api.model.Area
import com.therionlabs.capturadigital.api.model.Curso
import com.therionlabs.capturadigital.api.model.NivelEducativo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AreaRepository : JpaRepository<Area, Long>

@Repository
interface CursoRepository : JpaRepository<Curso, Long> {
    fun findByAreaId(areaId: Long): List<Curso>
}

@Repository
interface NivelEducativoRepository : JpaRepository<NivelEducativo, Long>