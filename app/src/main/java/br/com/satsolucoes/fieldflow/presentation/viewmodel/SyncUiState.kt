package br.com.satsolucoes.fieldflow.presentation.viewmodel

import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus

/**
 * Estado da tela de sincronização
 */
data class SyncUiState(
    val consumos: List<ConsumoSyncItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val pendentes: Int get() = consumos.count { it.status == SyncStatus.PENDING }
    val sincronizados: Int get() = consumos.count { it.status == SyncStatus.SYNCED }
    val comErro: Int get() = consumos.count { it.status == SyncStatus.ERROR }
    val total: Int get() = consumos.size
}

/**
 * Item de consumo para exibição na lista de sincronização
 */
data class ConsumoSyncItem(
    val id: Int,
    val materialNome: String,
    val quantidade: Double,
    val unidadeFormatada: String,
    val dataFormatada: String,
    val observacao: String,
    val status: SyncStatus
)


