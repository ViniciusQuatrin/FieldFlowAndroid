package br.com.satsolucoes.fieldflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.satsolucoes.fieldflow.domain.enums.UnidadeMedida
import br.com.satsolucoes.fieldflow.domain.model.Material

@Entity(tableName = "materials")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val quantidadeDisponivel: Double,
    val descricao: String?,
    val unidadeMedida: String = "UN"
) {
    fun toDomain(): Material = Material(
        id = id,
        nome = nome,
        quantidadeDisponivel = quantidadeDisponivel,
        descricao = descricao ?: "",
        unidadeMedida = UnidadeMedida.entries.find { it.simbolo == unidadeMedida } ?: UnidadeMedida.UNIDADE
    )

    companion object {
        fun fromDomain(material: Material): MaterialEntity = MaterialEntity(
            id = material.id,
            nome = material.nome,
            quantidadeDisponivel = material.quantidadeDisponivel,
            descricao = material.descricao,
            unidadeMedida = material.unidadeMedida.simbolo
        )
    }
}

