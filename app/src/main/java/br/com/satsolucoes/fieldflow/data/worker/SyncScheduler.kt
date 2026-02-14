package br.com.satsolucoes.fieldflow.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Gerenciador de agendamento de sincronização automática.
 *
 * Estratégia:
 * - OneTimeWork com constraint de rede: executa IMEDIATAMENTE quando conectar
 * - PeriodicWork como fallback: garantia de execução a cada 1h
 */
object SyncScheduler {

    private const val WORK_NAME_IMMEDIATE = "sync_consumos_immediate"
    private const val WORK_NAME_PERIODIC = "sync_consumos_periodic"

    /**
     * Agenda sincronização que executa assim que houver conectividade.
     * chamado na inicialização do app e após cada sync bem-sucedido.
     */
    fun agendarSincronizacaoImediata(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME_IMMEDIATE,
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }

    fun agendarSincronizacaoPeriodica(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    /**
     * Inicializa ambas as estratégias de sincronização.
     */
    fun inicializar(context: Context) {
        agendarSincronizacaoImediata(context)
        agendarSincronizacaoPeriodica(context)
    }

    /**
     * Cancela todas as sincronizações agendadas.
     */
    fun cancelarSincronizacao(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME_IMMEDIATE)
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME_PERIODIC)
    }
}

