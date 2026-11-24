package com.therionlabs.capturadigital.api

import com.therionlabs.capturadigital.api.dto.ProspectoRequest
import com.therionlabs.capturadigital.api.model.Prospecto
import com.therionlabs.capturadigital.api.repository.ProspectoRepository
import com.therionlabs.capturadigital.api.service.ProspectoService
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Pruebas unitarias para el servicio de prospectos (Spring Boot).
 */
class ProspectoServiceTest {

    private val prospectoRepository: ProspectoRepository = mockk()
    private lateinit var service: ProspectoService

    @BeforeEach
    fun setup() {
        service = ProspectoService(prospectoRepository)
        clearMocks(prospectoRepository)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(prospectoRepository)
    }

    @Test
    fun `crearProspecto should save and return prospect`() {
        val request = ProspectoRequest(
            nombre = "Carlos",
            correo = "carlos@test.com",
            telefono = "+56911111111",
            nivelEducativo = "Profesional",
            areaInteres = "Tecnología",
            cursoInteres = "Data Science",
            modalidad = "Online"
        )

        val savedProspecto = Prospecto(
            id = 1L,
            nombre = "Carlos",
            correo = "carlos@test.com",
            telefono = "+56911111111",
            fechaCaptura = java.time.Instant.now(),
            fechaSincronizacion = java.time.Instant.now()
        )

        every { prospectoRepository.save(any()) } returns savedProspecto

        val result = service.crearProspecto(request)

        assertNotNull(result)
        assertEquals("Carlos", result.nombre)
        assertEquals("carlos@test.com", result.correo)
        verify { prospectoRepository.save(any()) }
    }

    @Test
    fun `obtenerTodos should return all prospects`() {
        val prospectos = listOf(
            Prospecto(id = 1, nombre = "A", correo = "a@test.com", telefono = "1"),
            Prospecto(id = 2, nombre = "B", correo = "b@test.com", telefono = "2")
        )
        every { prospectoRepository.findAll() } returns prospectos

        val result = service.obtenerTodos()

        assertEquals(2, result.size)
    }

    @Test
    fun `obtenerPorId should return prospect when exists`() {
        val prospecto = Prospecto(id = 1L, nombre = "Test", correo = "test@test.com", telefono = "123")
        every { prospectoRepository.findById(1L) } returns java.util.Optional.of(prospecto)

        val result = service.obtenerPorId(1L)

        assertNotNull(result)
        assertEquals("Test", result?.nombre)
    }

    @Test
    fun `obtenerPorId should return null when not exists`() {
        every { prospectoRepository.findById(999L) } returns java.util.Optional.empty()

        val result = service.obtenerPorId(999L)

        assertNull(result)
    }

    @Test
    fun `crearProspecto should map all fields correctly`() {
        val request = ProspectoRequest(
            nombre = "Laura Muñoz",
            correo = "laura@correo.cl",
            telefono = "+56922222222",
            nivelEducativo = "Postgrado",
            areaInteres = "Negocios",
            cursoInteres = "Marketing Digital",
            modalidad = "Online",
            canalContacto = "WhatsApp",
            observaciones = "Contactar después de las 18hrs",
            idCaptador = "CPT-08",
            bateriaCaptura = 92
        )

        val captor = slot<Prospecto>()
        every { prospectoRepository.save(capture(captor)) } answers {
            captor.captured.copy(id = 1L)
        }

        val result = service.crearProspecto(request)

        assertEquals("Laura Muñoz", result.nombre)
        assertEquals("Online", result.modalidad)
        assertEquals("Marketing Digital", result.cursoInteres)
        assertEquals("CPT-08", result.idCaptador)
        assertEquals(92, result.bateriaCaptura)
    }
}