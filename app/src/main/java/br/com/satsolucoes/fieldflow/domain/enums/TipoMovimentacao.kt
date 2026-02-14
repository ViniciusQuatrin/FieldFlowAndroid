package br.com.satsolucoes.fieldflow.domain.enums

import com.google.gson.annotations.SerializedName

/**
 * Enum para tipo de movimentação de estoque.
 * Alinhado com o backend.
 */
enum class TipoMovimentacao {
    @SerializedName("ENTRADA")
    ENTRADA,
    @SerializedName("SAIDA")
    SAIDA
}

