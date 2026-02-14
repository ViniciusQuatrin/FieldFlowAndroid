package br.com.satsolucoes.fieldflow.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Black
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray100
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray400

/**
 * Card de material com controles de quantidade e observação.
 */
@Composable
fun MaterialCard(
    nome: String,
    estoqueFormatado: String,
    unidadeSimbolo: String,
    quantidadeConsumo: Double,
    observacao: String,
    usaInputManual: Boolean,
    podeIncrementar: Boolean,
    podeDecrementar: Boolean,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onQuantidadeChange: (Double) -> Unit,
    onObservacaoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Header: Nome + Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nome,
                    style = MaterialTheme.typography.titleLarge
                )
                StockBadge(text = estoqueFormatado)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Controles de quantidade
        if (usaInputManual) {
            // Campo editável para unidades contínuas (KG, M, L)
            QuantityInputField(
                quantidade = quantidadeConsumo,
                unidadeSimbolo = unidadeSimbolo,
                onQuantidadeChange = onQuantidadeChange
            )
        } else {
            // Botões +/- para unidades discretas (UN)
            QuantityButtonsRow(
                quantidade = quantidadeConsumo.toInt(),
                podeIncrementar = podeIncrementar,
                podeDecrementar = podeDecrementar,
                onIncrementar = onIncrementar,
                onDecrementar = onDecrementar
            )
        }

        if (quantidadeConsumo > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            ObservacaoField(
                observacao = observacao,
                onObservacaoChange = onObservacaoChange
            )
        }
    }
}

@Composable
private fun QuantityButtonsRow(
    quantidade: Int,
    podeIncrementar: Boolean,
    podeDecrementar: Boolean,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityButton(
            icon = Icons.Default.Remove,
            onClick = onDecrementar,
            isPrimary = false,
            enabled = podeDecrementar
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = quantidade.toString(),
                    style = MaterialTheme.typography.displayLarge
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(2.dp)
                        .background(Gray100)
                )
            }
        }

        QuantityButton(
            icon = Icons.Default.Add,
            onClick = onIncrementar,
            isPrimary = true,
            enabled = podeIncrementar
        )
    }
}

@Composable
private fun QuantityInputField(
    quantidade: Double,
    unidadeSimbolo: String,
    onQuantidadeChange: (Double) -> Unit
) {
    var textoLocal by remember { mutableStateOf("") }

    // Sincroniza com o valor externo quando muda
    LaunchedEffect(quantidade) {
        if (quantidade == 0.0) {
            textoLocal = ""
        } else {
            // Formata com vírgula
            val formatado = quantidade.toString().replace(".", ",")
            val limpo = if (formatado.endsWith(",0")) {
                formatado.dropLast(2)
            } else {
                formatado
            }
            if (textoLocal.replace(",", ".").toDoubleOrNull() != quantidade) {
                textoLocal = limpo
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textoLocal,
            onValueChange = { novoTexto ->
                val filtrado = novoTexto.filter { it.isDigit() || it == ',' || it == '.' }

                val normalizado = filtrado.replace(".", ",")

                val partes = normalizado.split(",")
                val textoValido = if (partes.size > 2) {
                    partes[0] + "," + partes.drop(1).joinToString("")
                } else {
                    normalizado
                }

                textoLocal = textoValido

                val valorParaConverter = textoValido.replace(",", ".")
                val valorDouble = valorParaConverter.toDoubleOrNull()
                if (valorDouble != null) {
                    onQuantidadeChange(valorDouble)
                } else if (textoValido.isEmpty()) {
                    onQuantidadeChange(0.0)
                }
            },
            modifier = Modifier.width(150.dp),
            textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.displayLarge,
                    color = Gray400,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = Gray100
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = unidadeSimbolo,
            style = MaterialTheme.typography.titleLarge,
            color = Gray400
        )
    }
}

@Composable
private fun ObservacaoField(
    observacao: String,
    onObservacaoChange: (String) -> Unit
) {
    OutlinedTextField(
        value = observacao,
        onValueChange = onObservacaoChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Adicionar Observação",
                color = Gray400
            )
        },
        singleLine = false,
        maxLines = 2,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Black,
            unfocusedBorderColor = Gray100
        )
    )
}

@Composable
fun MaterialCardDivider() {
    HorizontalDivider(
        color = Gray100,
        thickness = 1.dp
    )
}



