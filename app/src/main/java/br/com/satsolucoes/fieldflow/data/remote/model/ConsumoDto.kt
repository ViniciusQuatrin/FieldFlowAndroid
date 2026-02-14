package br.com.satsolucoes.fieldflow.data.remote.model

import br.com.satsolucoes.fieldflow.domain.enums.TipoMovimentacao
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data Transfer Object para enviar Consumo ao servidor.
 * Alinhado com MovimentacaoDTO do backend.
 */
data class ConsumoDto(
    val id: Long? = null,
    val tipo: TipoMovimentacao = TipoMovimentacao.SAIDA,
    val quantidade: BigDecimal,
    val dataHora: LocalDateTime? = null,
    val observacao: String? = null,
    val materialId: Long,
    val materialNome: String? = null
)
