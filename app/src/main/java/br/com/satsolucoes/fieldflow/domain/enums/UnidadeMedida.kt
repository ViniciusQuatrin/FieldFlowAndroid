package br.com.satsolucoes.fieldflow.domain.enums

enum class UnidadeMedida(
    val simbolo: String,
    val permiteDecimal: Boolean
) {
    UNIDADE("UN", permiteDecimal = false),
    QUILOGRAMA("KG", permiteDecimal = true),
    LITRO("L", permiteDecimal = true),
    METRO("M", permiteDecimal = true),
    CAIXA("CX", permiteDecimal = false);
    fun isQuantidadeValida(quantidade: Double): Boolean {
        return if (permiteDecimal) {
            quantidade >= 0
        } else {
            quantidade >= 0 && quantidade == quantidade.toLong().toDouble()
        }
    }

    fun formatarQuantidade(quantidade: Double): String {
        return if (permiteDecimal) {
            "%.2f $simbolo".format(quantidade)
        } else {
            "${quantidade.toLong()} $simbolo"
        }
    }
}

