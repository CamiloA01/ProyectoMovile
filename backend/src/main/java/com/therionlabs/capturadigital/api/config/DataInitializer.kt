package com.therionlabs.capturadigital.api.config

import com.therionlabs.capturadigital.api.model.Area
import com.therionlabs.capturadigital.api.model.Curso
import com.therionlabs.capturadigital.api.model.NivelEducativo
import com.therionlabs.capturadigital.api.repository.AreaRepository
import com.therionlabs.capturadigital.api.repository.CursoRepository
import com.therionlabs.capturadigital.api.repository.NivelEducativoRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Inicializa datos de catálogo en la base de datos al arrancar.
 */
@Component
class DataInitializer(
    private val areaRepository: AreaRepository,
    private val cursoRepository: CursoRepository,
    private val nivelEducativoRepository: NivelEducativoRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Solo sembrar si la BD está vacía
        if (areaRepository.count() > 0) return

        // Niveles educativos
        val niveles = listOf(
            "Básica", "Media", "Técnico Superior", "Profesional", "Postgrado"
        ).map { NivelEducativo(nombre = it) }
        nivelEducativoRepository.saveAll(niveles)

        // Áreas y cursos
        val tecnologia = areaRepository.save(Area(nombre = "Tecnología"))
        cursoRepository.saveAll(listOf(
            Curso(nombre = "Programación Web", descripcion = "Desarrollo de aplicaciones web modernas", areaId = tecnologia.id),
            Curso(nombre = "Ciberseguridad", descripcion = "Seguridad informática y protección de datos", areaId = tecnologia.id),
            Curso(nombre = "Data Science", descripcion = "Análisis de datos y machine learning", areaId = tecnologia.id),
            Curso(nombre = "Cloud Computing", descripcion = "Infraestructura y servicios en la nube", areaId = tecnologia.id)
        ))

        val negocios = areaRepository.save(Area(nombre = "Negocios"))
        cursoRepository.saveAll(listOf(
            Curso(nombre = "Marketing Digital", descripcion = "Estrategias de marketing online", areaId = negocios.id),
            Curso(nombre = "Finanzas Corporativas", descripcion = "Gestión financiera empresarial", areaId = negocios.id),
            Curso(nombre = "Gestión de Proyectos", descripcion = "Metodologías ágiles y tradicionales", areaId = negocios.id)
        ))

        val salud = areaRepository.save(Area(nombre = "Salud"))
        cursoRepository.saveAll(listOf(
            Curso(nombre = "Enfermería", descripcion = "Cuidados de enfermería", areaId = salud.id),
            Curso(nombre = "Nutrición", descripcion = "Alimentación saludable", areaId = salud.id),
            Curso(nombre = "Psicología", descripcion = "Salud mental", areaId = salud.id)
        ))

        val arte = areaRepository.save(Area(nombre = "Arte"))
        cursoRepository.saveAll(listOf(
            Curso(nombre = "Diseño Gráfico", descripcion = "Diseño visual y comunicación", areaId = arte.id),
            Curso(nombre = "Fotografía Digital", descripcion = "Técnicas fotográficas", areaId = arte.id),
            Curso(nombre = "Animación Digital", descripcion = "Animación 2D y 3D", areaId = arte.id)
        ))

        val idiomas = areaRepository.save(Area(nombre = "Idiomas"))
        cursoRepository.saveAll(listOf(
            Curso(nombre = "Inglés", descripcion = "Curso de inglés general", areaId = idiomas.id),
            Curso(nombre = "Portugués", descripcion = "Curso de portugués", areaId = idiomas.id),
            Curso(nombre = "Francés", descripcion = "Curso de francés", areaId = idiomas.id),
            Curso(nombre = "Alemán", descripcion = "Curso de alemán", areaId = idiomas.id)
        ))

        println("[DataInitializer] Catálogo académico sembrado exitosamente.")
    }
}