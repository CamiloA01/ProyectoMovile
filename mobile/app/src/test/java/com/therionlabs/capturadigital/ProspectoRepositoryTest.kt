package com.therionlabs.capturadigital

import com.therionlabs.capturadigital.data.local.dao.ProspectoDao
import com.therionlabs.capturadigital.data.local.dao.SyncLogDao
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import com.therionlabs.capturadigital.data.repository.ProspectoRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para el ProspectoRepository.
 * IL3.2: Integra pruebas unitarias para validar el comportamiento esperado.
 */
class ProspectoRepositoryTest {

    private val prospectoDao: ProspectoDao = mockk()
    private val syncLogDao: SyncLogDao = mockk()
    private lateinit var repository: ProspectoRepository

    @Before
    fun setup() {
        every { prospectoDao.getAllProspectos() } returns flowOf(emptyList())
        every { prospectoDao.getPendientes() } returns flowOf(emptyList())
        every { prospectoDao.getSincronizados() } returns flowOf(emptyList())
        every { prospectoDao.countAll() } returns flowOf(0)
        every { prospectoDao.countPendientes() } returns flowOf(0)
        every { syncLogDao.getAllLogs() } returns flowOf(emptyList())

        repository = ProspectoRepository(prospectoDao, syncLogDao)
    }

    @After
    fun tearDown() {
        clearMocks(prospectoDao, syncLogDao)
    }

    @Test
    fun `guardarLocal should insert prospecto and log in database`() = runTest {
        // Arrange
        val prospecto = ProspectoEntity(
            nombre = "Juan Pérez",
            correo = "juan@test.com",
            telefono = "+56912345678",
            nivelEducativo = "Profesional",
            areaInteres = "Tecnología",
            cursoInteres = "Programación Web",
            modalidad = "Online",
            canalContacto = "WhatsApp",
            fechaCaptura = System.currentTimeMillis()
        )
        coEvery { prospectoDao.insert(any()) } returns 1L
        coEvery { syncLogDao.insert(any()) } returns 1L

        // Act
        val id = repository.guardarLocal(prospecto)

        // Assert
        assertEquals(1L, id)
        coVerify { prospectoDao.insert(any()) }
        coVerify { syncLogDao.insert(any()) }
    }

    @Test
    fun `guardarLocal should preserve all prospect fields`() = runTest {
        // Arrange
        val prospecto = ProspectoEntity(
            nombre = "María García",
            correo = "maria@ejemplo.com",
            telefono = "+56998765432",
            nivelEducativo = "Técnico Superior",
            areaInteres = "Salud",
            cursoInteres = "Enfermería",
            modalidad = "Presencial",
            canalContacto = "Email",
            fechaCaptura = 123456789L,
            observaciones = "Interesada en horario vespertino",
            idCaptador = "CPT-05",
            bateriaCaptura = 85
        )
        coEvery { prospectoDao.insert(any()) } returns 1L
        coEvery { syncLogDao.insert(any()) } returns 1L

        // Act
        val id = repository.guardarLocal(prospecto)

        // Assert
        assertTrue(id > 0)
        coVerify {
            prospectoDao.insert(match {
                it.nombre == "María García" &&
                it.correo == "maria@ejemplo.com" &&
                it.cursoInteres == "Enfermería" &&
                it.modalidad == "Presencial" &&
                it.idCaptador == "CPT-05" &&
                it.bateriaCaptura == 85
            })
        }
    }

    @Test
    fun `prospecto data class should have correct defaults`() {
        // Arrange & Act
        val prospecto = ProspectoEntity(
            nombre = "Test",
            correo = "test@ejemplo.cl",
            telefono = "+56912345678",
            nivelEducativo = "Profesional",
            areaInteres = "Tecnología",
            cursoInteres = "Data Science",
            modalidad = "Online",
            canalContacto = "WhatsApp",
            fechaCaptura = 1000L
        )

        // Assert
        assertEquals(0L, prospecto.id) // auto-generado
        assertFalse(prospecto.sincronizado) // default: pendiente
        assertEquals("", prospecto.observaciones) // default
        assertEquals("", prospecto.idCaptador) // default
    }

    @Test
    fun `guardarLocal with empty fields should still work`() = runTest {
        // Arrange - campos vacíos pero válidos (solo los obligatorios)
        val prospecto = ProspectoEntity(
            nombre = "Minimal",
            correo = "min@test.com",
            telefono = "000",
            nivelEducativo = "",
            areaInteres = "",
            cursoInteres = "",
            modalidad = "",
            canalContacto = "",
            fechaCaptura = System.currentTimeMillis()
        )
        coEvery { prospectoDao.insert(any()) } returns 42L
        coEvery { syncLogDao.insert(any()) } returns 1L

        // Act
        val id = repository.guardarLocal(prospecto)

        // Assert
        assertEquals(42L, id)
    }

    @Test
    fun `multiple saves should use different IDs`() = runTest {
        // Arrange
        coEvery { prospectoDao.insert(any()) } returnsMany listOf(1L, 2L, 3L)
        coEvery { syncLogDao.insert(any()) } returns 1L

        val base = ProspectoEntity(
            nombre = "User", correo = "u@t.com", telefono = "1",
            nivelEducativo = "", areaInteres = "", cursoInteres = "",
            modalidad = "", canalContacto = "",
            fechaCaptura = System.currentTimeMillis()
        )

        // Act
        val id1 = repository.guardarLocal(base)
        val id2 = repository.guardarLocal(base.copy())
        val id3 = repository.guardarLocal(base.copy())

        // Assert
        assertEquals(1L, id1)
        assertEquals(2L, id2)
        assertEquals(3L, id3)
    }
}