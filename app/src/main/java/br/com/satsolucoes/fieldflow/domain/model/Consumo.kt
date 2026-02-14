package br.com.satsolucoes.fieldflow.domain.model

import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import java.time.LocalDateTime

data class  Consumo(
    val id: Int = 0,
    val materialId: Int,
    val quantidadeConsumida: Double,
    val statusSync: SyncStatus = SyncStatus.PENDING,
    val observacao: String = "",
    val data: LocalDateTime
)


