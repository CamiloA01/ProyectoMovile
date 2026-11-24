package com.therionlabs.capturadigital.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.therionlabs.capturadigital.data.local.dao.ProspectoDao
import com.therionlabs.capturadigital.data.local.dao.SyncLogDao
import com.therionlabs.capturadigital.data.local.entity.ProspectoEntity
import com.therionlabs.capturadigital.data.local.entity.SyncLogEntity

/**
 * Base de datos Room para persistencia offline.
 */
@Database(
    entities = [ProspectoEntity::class, SyncLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CapturaDatabase : RoomDatabase() {

    abstract fun prospectoDao(): ProspectoDao
    abstract fun syncLogDao(): SyncLogDao

    companion object {
        @Volatile
        private var INSTANCE: CapturaDatabase? = null

        fun getDatabase(context: Context): CapturaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CapturaDatabase::class.java,
                    "captura_digital_db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}