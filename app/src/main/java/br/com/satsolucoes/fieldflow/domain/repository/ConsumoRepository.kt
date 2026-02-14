package br.com.satsolucoes.fieldflow.domain.repository

import br.com.satsolucoes.fieldflow.domain.model.Consumo
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import kotlinx.coroutines.flow.Flow

interface ConsumoRepository {

    suspend fun registrarConsumo(consumo: Consumo)

    suspend fun atualizarConsumo(consumo: Consumo)

    suspend fun deletarConsumo(consumo: Consumo)

    suspend fun obterConsumoPorId(id: Int): Consumo?

    fun obterTodosConsumos(): Flow<List<Consumo>>

    fun obterConsumosPorMaterial(materialId: Int): Flow<List<Consumo>>

    fun obterConsumosPendentes(): Flow<List<Consumo>>

    suspend fun obterConsumosPendentesLista(): List<Consumo>

    suspend fun atualizarStatusSync(consumoId: Int, status: SyncStatus)

    suspend fun marcarComoSincronizado(consumoId: Int)

    suspend fun marcarComoErro(consumoId: Int)
}

