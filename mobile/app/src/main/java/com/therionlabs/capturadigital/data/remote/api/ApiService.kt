package com.therionlabs.capturadigital.data.remote.api

import com.therionlabs.capturadigital.data.remote.dto.CatalogosResponse
import com.therionlabs.capturadigital.data.remote.dto.ProspectoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz Retrofit para la API REST del backend Spring Boot.
 */
interface ApiService {

    @POST("api/prospectos")
    suspend fun enviarProspecto(@Body prospecto: ProspectoRequest): Response<Map<String, Any>>

    @GET("api/prospectos")
    suspend fun obtenerProspectos(): Response<List<ProspectoRequest>>

    @GET("api/catalogos")
    suspend fun obtenerCatalogos(): Response<CatalogosResponse>

    @GET("api/health")
    suspend fun healthCheck(): Response<Map<String, Any>>
}