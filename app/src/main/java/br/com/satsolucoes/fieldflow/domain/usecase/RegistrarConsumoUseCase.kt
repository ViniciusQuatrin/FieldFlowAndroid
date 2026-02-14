package br.com.satsolucoes.fieldflow.domain.usecase

import br.com.satsolucoes.fieldflow.domain.model.Consumo
import br.com.satsolucoes.fieldflow.domain.model.DomainError
import br.com.satsolucoes.fieldflow.domain.repository.ConsumoRepository
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository

/**
 * Use Case para registrar um consumo de material
 * Responsável por validar regras de negócio e atualizar o estoque
 */
class RegistrarConsumoUseCase(
    private val consumoRepository: ConsumoRepository,
    private val materialRepository: MaterialRepository
) {
    suspend operator fun invoke(consumo: Consumo): Result<Unit> {
        return try {
            if (consumo.quantidadeConsumida <= 0) {
                return Result.failure(DomainError.ConsumoError.QuantidadeInvalida)
            }

            val material = materialRepository.obterMaterialPorId(consumo.materialId)
                ?: return Result.failure(DomainError.MaterialError.NaoEncontrado)

            if (material.quantidadeDisponivel < consumo.quantidadeConsumida) {
                return Result.failure(
                    DomainError.MaterialError.EstoqueInsuficiente(
                        disponivel = material.quantidadeDisponivel,
                        unidade = material.unidadeMedida
                    )
                )
            }

            consumoRepository.registrarConsumo(consumo)
            materialRepository.consumirEstoque(consumo.materialId, consumo.quantidadeConsumida)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


