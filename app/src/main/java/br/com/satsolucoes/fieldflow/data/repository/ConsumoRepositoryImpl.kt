package br.com.satsolucoes.fieldflow.data.repository

import br.com.satsolucoes.fieldflow.data.local.dao.ConsumoDao
import br.com.satsolucoes.fieldflow.data.local.entity.ConsumoEntity
import br.com.satsolucoes.fieldflow.domain.model.Consumo
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.domain.repository.ConsumoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConsumoRepositoryImpl(
    private val consumoDao: ConsumoDao
) : ConsumoRepository {

    override suspend fun registrarConsumo(consumo: Consumo) {
        consumoDao.inserir(ConsumoEntity.fromDomain(consumo))
    }

    override suspend fun atualizarConsumo(consumo: Consumo) {
        consumoDao.atualizar(ConsumoEntity.fromDomain(consumo))
    }

    override suspend fun deletarConsumo(consumo: Consumo) {
        consumoDao.deletar(ConsumoEntity.fromDomain(consumo))
    }

    override suspend fun obterConsumoPorId(id: Int): Consumo? {
        return consumoDao.obterPorId(id)?.toDomain()
    }

    override fun obterTodosConsumos(): Flow<List<Consumo>> {
        return consumoDao.obterTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun obterConsumosPorMaterial(materialId: Int): Flow<List<Consumo>> {
        return consumoDao.obterPorMaterial(materialId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun obterConsumosPendentes(): Flow<List<Consumo>> {
        return consumoDao.obterPendentes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun obterConsumosPendentesLista(): List<Consumo> {
        return consumoDao.obterPendentesLista().map { it.toDomain() }
    }

    override suspend fun atualizarStatusSync(consumoId: Int, status: SyncStatus) {
        consumoDao.atualizarStatusSync(consumoId, status.name)
    }

    override suspend fun marcarComoSincronizado(consumoId: Int) {
        consumoDao.atualizarStatusSync(consumoId, SyncStatus.SYNCED.name)
    }

    override suspend fun marcarComoErro(consumoId: Int) {
        consumoDao.atualizarStatusSync(consumoId, SyncStatus.ERROR.name)
    }
}

