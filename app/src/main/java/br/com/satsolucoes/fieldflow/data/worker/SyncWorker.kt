package br.com.satsolucoes.fieldflow.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.satsolucoes.fieldflow.data.local.dao.ConsumoDao
import br.com.satsolucoes.fieldflow.data.remote.api.MaterialApiService
import br.com.satsolucoes.fieldflow.data.remote.model.ConsumoDto
import br.com.satsolucoes.fieldflow.domain.enums.TipoMovimentacao
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Worker responsável por sincronizar consumos pendentes com o servidor. Executado automaticamente
 * pelo WorkManager quando há conectividade.
 */
class SyncWorker(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams), KoinComponent {

    private val consumoDao: ConsumoDao by inject()
    private val apiService: MaterialApiService by inject()

    override suspend fun doWork(): Result =
            withContext(Dispatchers.IO) {
                try {
                    val pendentes = consumoDao.obterPendentesLista()

                    if (pendentes.isEmpty()) {
                        return@withContext Result.success()
                    }

                    pendentes.forEach { consumo ->
                        val consumoDto = ConsumoDto(
                            tipo = TipoMovimentacao.SAIDA,
                            quantidade = BigDecimal.valueOf(consumo.quantidadeConsumida),
                            materialId = consumo.materialId.toLong(),
                            observacao = consumo.observacao,
                            dataHora = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(consumo.data),
                                ZoneOffset.UTC
                            )
                        )

                        val sucesso =
                                try {
                                    val response = apiService.postConsumo(consumoDto)
                                    response.isSuccessful
                                } catch (e: Exception) {
                                    false
                                }

                        if (sucesso) {
                            consumoDao.atualizarStatusSync(consumo.id, SyncStatus.SYNCED.name)
                        } else {
                            consumoDao.atualizarStatusSync(consumo.id, SyncStatus.ERROR.name)
                        }
                    }

                    SyncScheduler.agendarSincronizacaoImediata(applicationContext)

                    Result.success()
                } catch (_: Exception) {
                    if (runAttemptCount < MAX_RETRIES) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
            }

    companion object {
        const val WORK_NAME = "sync_consumos_work"
        private const val MAX_RETRIES = 3
    }
}
