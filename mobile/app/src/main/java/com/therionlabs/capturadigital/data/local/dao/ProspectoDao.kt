package com.therionlabs.capturadigital.data.local.dao

import androidx.room.*
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProspectoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prospecto: ProspectoEntity): Long

    @Update
    suspend fun update(prospecto: ProspectoEntity)

    @Delete
    suspend fun delete(prospecto: ProspectoEntity)

    @Query("SELECT * FROM prospectos ORDER BY fechaCaptura DESC")
    fun getAllProspectos(): Flow<List<ProspectoEntity>>

    @Query("SELECT * FROM prospectos WHERE sincronizado = 0 ORDER BY fechaCaptura DESC")
    fun getPendientes(): Flow<List<ProspectoEntity>>

    @Query("SELECT * FROM prospectos WHERE sincronizado = 1 ORDER BY fechaCaptura DESC")
    fun getSincronizados(): Flow<List<ProspectoEntity>>

    @Query("SELECT * FROM prospectos WHERE id = :id")
    suspend fun getById(id: Long): ProspectoEntity?

    @Query("UPDATE prospectos SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarSincronizado(id: Long)

    @Query("SELECT COUNT(*) FROM prospectos")
    fun countAll(): Flow<Int>

    @Query("SELECT COUNT(*) FROM prospectos WHERE sincronizado = 0")
    fun countPendientes(): Flow<Int>

    @Query("SELECT * FROM prospectos WHERE sincronizado = 0")
    suspend fun getPendientesList(): List<ProspectoEntity>
}