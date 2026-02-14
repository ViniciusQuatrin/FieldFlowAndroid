package br.com.satsolucoes.fieldflow.domain.model

import br.com.satsolucoes.fieldflow.domain.enums.UnidadeMedida

/**
 * Hierarquia de erros de domínio tipados
 */
sealed class DomainError(override val message: String) : Exception(message) {

    // Erros de Material
    sealed class MaterialError(message: String) : DomainError(message) {
        object NomeVazio : MaterialError("Nome do material não pode estar vazio")
        object QuantidadeNegativa : MaterialError("Quantidade não pode ser negativa")
        object NaoEncontrado : MaterialError("Material não encontrado")
        object QuantidadeIncompativelComUnidade : MaterialError("Quantidade inválida para a unidade de medida")
        data class EstoqueInsuficiente(
            val disponivel: Double,
            val unidade: UnidadeMedida
        ) : MaterialError("Estoque insuficiente. Disponível: ${unidade.formatarQuantidade(disponivel)}")
    }

    // Erros de Consumo
    sealed class ConsumoError(message: String) : DomainError(message) {
        object QuantidadeInvalida : ConsumoError("Quantidade deve ser maior que zero")
    }
}


