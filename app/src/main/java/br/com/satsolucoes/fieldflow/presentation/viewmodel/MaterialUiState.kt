package br.com.satsolucoes.fieldflow.presentation.viewmodel

import br.com.satsolucoes.fieldflow.domain.enums.UnidadeMedida
import br.com.satsolucoes.fieldflow.domain.model.Material

data class MaterialUiState(
    val materiais: List<MaterialItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class MaterialItemState(
    val material: Material,
    val quantidadeConsumo: Double = 0.0,
    val observacao: String = ""
) {
    val id: Int get() = material.id
    val nome: String get() = material.nome
    val quantidadeDisponivel: Double get() = material.quantidadeDisponivel
    val unidadeMedida: UnidadeMedida get() = material.unidadeMedida
    val estoqueFormatado: String get() = unidadeMedida.formatarQuantidade(quantidadeDisponivel)

    /** Se true, usa campo de texto editável. Se false, usa botões +/- */
    val usaInputManual: Boolean get() = unidadeMedida.permiteDecimal

    val podeIncrementar: Boolean get() = !usaInputManual && quantidadeConsumo < quantidadeDisponivel
    val podeDecrementar: Boolean get() = !usaInputManual && quantidadeConsumo > 0
}

