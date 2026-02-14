package br.com.satsolucoes.fieldflow.domain.model

import br.com.satsolucoes.fieldflow.domain.enums.UnidadeMedida

data class Material(
    val id: Int = 0,
    val nome: String,
    val quantidadeDisponivel: Double,
    val descricao: String,
    val unidadeMedida: UnidadeMedida
)
