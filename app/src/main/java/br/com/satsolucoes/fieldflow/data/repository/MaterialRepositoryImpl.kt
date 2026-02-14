package br.com.satsolucoes.fieldflow.data.repository

import br.com.satsolucoes.fieldflow.data.local.dao.MaterialDao
import br.com.satsolucoes.fieldflow.data.local.entity.MaterialEntity
import br.com.satsolucoes.fieldflow.data.remote.api.MaterialApiService
import br.com.satsolucoes.fieldflow.domain.model.Material
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MaterialRepositoryImpl(
    private val materialDao: MaterialDao,
    private val apiService: MaterialApiService
) : MaterialRepository {

    override suspend fun inserirMaterial(material: Material) {
        materialDao.inserir(MaterialEntity.fromDomain(material))
    }

    override suspend fun atualizarMaterial(material: Material) {
        materialDao.atualizar(MaterialEntity.fromDomain(material))
    }

    override suspend fun deletarMaterial(material: Material) {
        materialDao.deletar(MaterialEntity.fromDomain(material))
    }

    override suspend fun obterMaterialPorId(id: Int): Material? {
        return materialDao.obterPorId(id)?.toDomain()
    }

    override fun obterTodosMateriais(): Flow<List<Material>> {
        return materialDao.obterTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun obterMateriaisComEstoqueBaixo(limite: Double): Flow<List<Material>> {
        return materialDao.obterComEstoqueBaixo(limite).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun atualizarEstoque(materialId: Int, novaQuantidade: Double) {
        materialDao.atualizarEstoque(materialId, novaQuantidade)
    }

    override suspend fun consumirEstoque(materialId: Int, quantidadeConsumida: Double): Boolean {
        val material = materialDao.obterPorId(materialId) ?: return false
        val novaQuantidade = material.quantidadeDisponivel - quantidadeConsumida
        if (novaQuantidade < 0) return false
        materialDao.atualizarEstoque(materialId, novaQuantidade)
        return true
    }

    /**
     * Sincroniza materiais do servidor para o banco local.
     */
    suspend fun sincronizarDoServidor(): Result<Int> {
        return try {
            val materiaisRemotos = apiService.fetchMaterials()
            var atualizados = 0

            materiaisRemotos.forEach { dto ->
                val local = materialDao.obterPorId(dto.id)

                if (local == null) {
                    // Material novo - inserir
                    materialDao.inserir(dto.toEntity())
                    atualizados++
                } else {
                    // Material existe - atualizar metadados (nome, descrição, unidade)
                    // NÃO atualiza estoque pois o técnico pode ter consumido localmente
                    val entityAtualizada = local.copy(
                        nome = dto.nome,
                        descricao = dto.descricao,
                        unidadeMedida = dto.unidadeMedida
                    )
                    if (entityAtualizada != local) {
                        materialDao.atualizar(entityAtualizada)
                        atualizados++
                    }
                }
            }

            Result.success(atualizados)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sincronizarMateriais(): Boolean {
        return try {
            val resultado = sincronizarDoServidor()
            resultado.isSuccess
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Limpa todos os materiais do banco local e ressincroniza da fonte atual.
     * Usado quando o usuário alterna entre Fake e Server API.
     */
    suspend fun limparERessincronizar(): Result<Int> {
        return try {
            // Limpa todos os materiais do banco local
            materialDao.deletarTodos()

            // Busca e insere os novos materiais da fonte atual
            val materiaisRemotos = apiService.fetchMaterials()
            materiaisRemotos.forEach { dto ->
                materialDao.inserir(dto.toEntity())
            }

            Result.success(materiaisRemotos.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

