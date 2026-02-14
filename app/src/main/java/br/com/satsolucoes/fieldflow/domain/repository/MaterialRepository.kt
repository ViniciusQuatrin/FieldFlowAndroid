package br.com.satsolucoes.fieldflow.domain.repository

import br.com.satsolucoes.fieldflow.domain.model.Material
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {

    suspend fun inserirMaterial(material: Material)

    suspend fun atualizarMaterial(material: Material)

    suspend fun deletarMaterial(material: Material)

    suspend fun obterMaterialPorId(id: Int): Material?

    fun obterTodosMateriais(): Flow<List<Material>>

    fun obterMateriaisComEstoqueBaixo(limite: Double = 10.0): Flow<List<Material>>

    suspend fun atualizarEstoque(materialId: Int, novaQuantidade: Double)

    suspend fun consumirEstoque(materialId: Int, quantidadeConsumida: Double): Boolean
}

