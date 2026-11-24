package com.therionlabs.capturadigital.data.local.dao

import androidx.room.*
import com.therionlabs.capturadigital.data.local.entity.SyncLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncLogDao {

    @Insert
    suspend fun insert(log: SyncLogEntity): Long

    @Query("SELECT * FROM sync_logs WHERE prospectoId = :prospectoId ORDER BY timestamp DESC")
    fun getLogsForProspecto(prospectoId: Long): Flow<List<SyncLogEntity>>

    @Query("SELECT * FROM sync_logs ORDER BY timestamp DESC LIMIT 50")
    fun getAllLogs(): Flow<List<SyncLogEntity>>

    @Query("DELETE FROM sync_logs WHERE prospectoId = :prospectoId")
    suspend fun deleteLogsForProspecto(prospectoId: Long)
}