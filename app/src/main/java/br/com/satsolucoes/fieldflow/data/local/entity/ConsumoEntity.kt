package br.com.satsolucoes.fieldflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.domain.model.Consumo
import java.time.LocalDateTime

@Entity(
    tableName = "consumos",
    foreignKeys = [
        ForeignKey(
            entity = MaterialEntity::class,
            parentColumns = ["id"],
            childColumns = ["materialId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["materialId"])]
)
data class ConsumoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val materialId: Int,
    val quantidadeConsumida: Double,
    val statusSync: String = SyncStatus.PENDING.name,
    val observacao: String = "",
    val data: Long
) {
    fun toDomain(): Consumo = Consumo(
        id = id,
        materialId = materialId,
        quantidadeConsumida = quantidadeConsumida,
        statusSync = SyncStatus.valueOf(statusSync),
        observacao = observacao,
        data = LocalDateTime.ofEpochSecond(data / 1000, 0, java.time.ZoneOffset.UTC)
    )

    companion object {
        fun fromDomain(consumo: Consumo): ConsumoEntity = ConsumoEntity(
            id = consumo.id,
            materialId = consumo.materialId,
            quantidadeConsumida = consumo.quantidadeConsumida,
            statusSync = consumo.statusSync.name,
            observacao = consumo.observacao,
            data = consumo.data.toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        )
    }
}

