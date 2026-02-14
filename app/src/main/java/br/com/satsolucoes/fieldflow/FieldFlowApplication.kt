package br.com.satsolucoes.fieldflow

import android.app.Application
import br.com.satsolucoes.fieldflow.data.remote.config.ApiConfig
import br.com.satsolucoes.fieldflow.data.worker.SyncScheduler
import br.com.satsolucoes.fieldflow.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FieldFlowApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initApiConfig()
        initKoin()
        initSyncScheduler()
    }

    private fun initApiConfig() {
        ApiConfig.init(this)
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@FieldFlowApplication)
            modules(appModules)
        }
    }

    private fun initSyncScheduler() {
        SyncScheduler.inicializar(this)
    }
}

