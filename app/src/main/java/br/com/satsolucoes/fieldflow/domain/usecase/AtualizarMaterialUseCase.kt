package br.com.satsolucoes.fieldflow.domain.usecase

import br.com.satsolucoes.fieldflow.domain.model.Material
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository
import br.com.satsolucoes.fieldflow.domain.validation.MaterialValidator

/**
 * Use Case para atualizar um material existente
 */
class AtualizarMaterialUseCase(
    private val repository: MaterialRepository
) {
    suspend operator fun invoke(material: Material): Result<Unit> {
        return try {
            MaterialValidator.validar(material).getOrElse { return Result.failure(it) }

            repository.atualizarMaterial(material)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

