package com.therionlabs.capturadigital.api.repository

import com.therionlabs.capturadigital.api.model.Prospecto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ProspectoRepository : JpaRepository<Prospecto, Long> {

    fun findByCorreo(correo: String): List<Prospecto>

    fun findByIdCaptador(idCaptador: String): List<Prospecto>

    fun findByFechaCapturaBetween(inicio: Instant, fin: Instant): List<Prospecto>
}