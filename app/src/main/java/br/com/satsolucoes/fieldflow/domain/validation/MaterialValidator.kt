package br.com.satsolucoes.fieldflow.domain.validation

import br.com.satsolucoes.fieldflow.domain.model.DomainError
import br.com.satsolucoes.fieldflow.domain.model.Material

object MaterialValidator {

    fun validar(material: Material): Result<Unit> {
        return when {
            material.nome.isBlank() -> Result.failure(DomainError.MaterialError.NomeVazio)
            material.quantidadeDisponivel < 0 -> Result.failure(DomainError.MaterialError.QuantidadeNegativa)
            !material.unidadeMedida.isQuantidadeValida(material.quantidadeDisponivel) ->
                Result.failure(DomainError.MaterialError.QuantidadeIncompativelComUnidade)
            else -> Result.success(Unit)
        }
    }
}

