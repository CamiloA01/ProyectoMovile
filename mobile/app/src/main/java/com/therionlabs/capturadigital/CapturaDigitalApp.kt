package com.therionlabs.capturadigital

import android.app.Application
import com.therionlabs.capturadigital.data.local.CapturaDatabase

/**
 * Application class de Captura Digital.
 * Inicializa la base de datos Room al arrancar la app.
 */
class CapturaDigitalApp : Application() {

    lateinit var database: CapturaDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = CapturaDatabase.getDatabase(this)
    }
}