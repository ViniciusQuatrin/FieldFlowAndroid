package br.com.satsolucoes.fieldflow.presentation.ui.screens.materials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.domain.enums.UnidadeMedida
import br.com.satsolucoes.fieldflow.domain.model.Material
import br.com.satsolucoes.fieldflow.presentation.ui.components.ApiModeSelector
import br.com.satsolucoes.fieldflow.presentation.ui.components.MaterialCard
import br.com.satsolucoes.fieldflow.presentation.ui.components.MaterialCardDivider
import br.com.satsolucoes.fieldflow.presentation.viewmodel.MaterialItemState
import br.com.satsolucoes.fieldflow.presentation.viewmodel.MaterialUiState
import br.com.satsolucoes.fieldflow.presentation.viewmodel.MaterialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MaterialsScreen(
    modifier: Modifier = Modifier,
    viewModel: MaterialViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialsScreenContent(
        uiState = uiState,
        onIncrementar = viewModel::incrementarConsumo,
        onDecrementar = viewModel::decrementarConsumo,
        onQuantidadeChange = viewModel::setarQuantidadeConsumo,
        onObservacaoChange = viewModel::setarObservacao,
        onApiModeChanged = viewModel::forcarRessincronizacao,
        modifier = modifier
    )
}

@Composable
fun MaterialsScreenContent(
    uiState: MaterialUiState,
    onIncrementar: (Int) -> Unit,
    onDecrementar: (Int) -> Unit,
    onQuantidadeChange: (Int, Double) -> Unit,
    onObservacaoChange: (Int, String) -> Unit,
    onApiModeChanged: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Header
        MaterialsHeader(onApiModeChanged = onApiModeChanged)

        // Content
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            uiState.materiais.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum material cadastrado",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                MaterialsList(
                    materiais = uiState.materiais,
                    onIncrementar = onIncrementar,
                    onDecrementar = onDecrementar,
                    onQuantidadeChange = onQuantidadeChange,
                    onObservacaoChange = onObservacaoChange
                )
            }
        }
    }
}

@Composable
private fun MaterialsHeader(
    onApiModeChanged: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Materiais",
            style = MaterialTheme.typography.headlineLarge
        )

        ApiModeSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            onModeChanged = onApiModeChanged
        )
    }
}

@Composable
private fun MaterialsList(
    materiais: List<MaterialItemState>,
    onIncrementar: (Int) -> Unit,
    onDecrementar: (Int) -> Unit,
    onQuantidadeChange: (Int, Double) -> Unit,
    onObservacaoChange: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
    ) {
        itemsIndexed(
            items = materiais,
            key = { _, item -> item.id }
        ) { index, item ->
            MaterialCard(
                nome = item.nome,
                estoqueFormatado = item.estoqueFormatado,
                unidadeSimbolo = item.unidadeMedida.simbolo,
                quantidadeConsumo = item.quantidadeConsumo,
                observacao = item.observacao,
                usaInputManual = item.usaInputManual,
                podeIncrementar = item.podeIncrementar,
                podeDecrementar = item.podeDecrementar,
                onIncrementar = { onIncrementar(item.id) },
                onDecrementar = { onDecrementar(item.id) },
                onQuantidadeChange = { quantidade -> onQuantidadeChange(item.id, quantidade) },
                onObservacaoChange = { obs -> onObservacaoChange(item.id, obs) }
            )

            if (index < materiais.lastIndex) {
                MaterialCardDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaterialsScreenPreviewLoading() {
    MaterialsScreenContent(
        uiState = MaterialUiState(isLoading = true),
        onIncrementar = {},
        onDecrementar = {},
        onQuantidadeChange = { _, _ -> },
        onObservacaoChange = { _, _ -> },
        onApiModeChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MaterialsScreenPreviewEmpty() {
    MaterialsScreenContent(
        uiState = MaterialUiState(materiais = emptyList()),
        onIncrementar = {},
        onDecrementar = {},
        onQuantidadeChange = { _, _ -> },
        onObservacaoChange = { _, _ -> },
        onApiModeChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MaterialsScreenPreviewWithContent() {
    val sampleMaterials = listOf(
        Material(
            id = 1,
            nome = "Cabo Flexível 2.5mm",
            quantidadeDisponivel = 100.0,
            descricao = "Rolo de 100m - Preto",
            unidadeMedida = UnidadeMedida.METRO
        ),
        Material(
            id = 2,
            nome = "Disjuntor 20A",
            quantidadeDisponivel = 15.0,
            descricao = "Unipolar Weg 220V",
            unidadeMedida = UnidadeMedida.UNIDADE
        )
    )
    val sampleMaterialItems = sampleMaterials.mapIndexed { index, material ->
        MaterialItemState(
            material = material,
            quantidadeConsumo = if (index == 0) 5.5 else 2.0,
            observacao = if (index == 0) "Usado no andar 2" else ""
        )
    }

    MaterialsScreenContent(
        uiState = MaterialUiState(materiais = sampleMaterialItems),
        onIncrementar = {},
        onDecrementar = {},
        onQuantidadeChange = { _, _ -> },
        onObservacaoChange = { _, _ -> },
        onApiModeChanged = {}
    )
}
