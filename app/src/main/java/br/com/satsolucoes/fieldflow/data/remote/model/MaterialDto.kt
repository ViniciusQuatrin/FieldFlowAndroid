package br.com.satsolucoes.fieldflow.data.remote.model

import br.com.satsolucoes.fieldflow.data.local.entity.MaterialEntity

/** Data Transfer Object para Material vindo do servidor */
data class MaterialDto(
    val id: Int,
    val nome: String,
    val quantidadeDisponivel: Double,
    val descricao: String?,
    val unidadeMedida: String = "UN"
) {
    fun toEntity(): MaterialEntity = MaterialEntity(
        id = id,
        nome = nome,
        quantidadeDisponivel = quantidadeDisponivel,
        descricao = descricao,
        unidadeMedida = unidadeMedida
    )
}
