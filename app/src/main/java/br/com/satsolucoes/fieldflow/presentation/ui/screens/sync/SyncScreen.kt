package br.com.satsolucoes.fieldflow.presentation.ui.screens.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.presentation.ui.components.SyncItemCard
import br.com.satsolucoes.fieldflow.presentation.ui.components.SyncSummaryRow
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray400
import br.com.satsolucoes.fieldflow.presentation.ui.theme.Gray800
import br.com.satsolucoes.fieldflow.presentation.ui.theme.White
import br.com.satsolucoes.fieldflow.presentation.viewmodel.ConsumoSyncItem
import br.com.satsolucoes.fieldflow.presentation.viewmodel.SyncUiState
import br.com.satsolucoes.fieldflow.presentation.viewmodel.SyncViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SyncScreen(
    modifier: Modifier = Modifier,
    viewModel: SyncViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SyncScreenContent(
        uiState = uiState,
        onReenviarErros = viewModel::reenviarComErro,
        modifier = modifier
    )
}

@Composable
fun SyncScreenContent(
    uiState: SyncUiState,
    onReenviarErros: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Text(
                text = "Sincronização",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // Resumo
            if (!uiState.isLoading && uiState.consumos.isNotEmpty()) {
                SyncSummaryRow(
                    pendentes = uiState.pendentes,
                    sincronizados = uiState.sincronizados,
                    comErro = uiState.comErro
                )
            }

            // Content
            when {
                uiState.isLoading -> LoadingState()
                uiState.error != null -> ErrorState(message = uiState.error)
                uiState.consumos.isEmpty() -> EmptyState()
                else -> SyncList(consumos = uiState.consumos)
            }
        }

        // FAB para reenviar itens com erro
        if (uiState.comErro > 0) {
            FloatingActionButton(
                onClick = onReenviarErros,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                shape = CircleShape,
                containerColor = Gray800,
                contentColor = White
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reenviar itens com erro",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Gray400
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nenhum consumo registrado",
                style = MaterialTheme.typography.bodyMedium,
                color = Gray400
            )
        }
    }
}

@Composable
private fun SyncList(consumos: List<ConsumoSyncItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = consumos, key = { it.id }) { item ->
            SyncItemCard(
                materialNome = item.materialNome,
                unidadeFormatada = item.unidadeFormatada,
                dataFormatada = item.dataFormatada,
                observacao = item.observacao,
                status = item.status
            )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun SyncScreenPreviewLoading() {
    SyncScreenContent(uiState = SyncUiState(isLoading = true), onReenviarErros = {})
}

@Preview(showBackground = true)
@Composable
private fun SyncScreenPreviewEmpty() {
    SyncScreenContent(uiState = SyncUiState(consumos = emptyList()), onReenviarErros = {})
}

@Preview(showBackground = true)
@Composable
private fun SyncScreenPreviewWithContent() {
    val sampleItems = listOf(
        ConsumoSyncItem(1, "Cabo Flexível 2.5mm", 15.0, "15.00 M", "12/02/2026 14:30", "Usado no andar 2", SyncStatus.SYNCED),
        ConsumoSyncItem(2, "Disjuntor 20A", 2.0, "2 UN", "12/02/2026 14:25", "", SyncStatus.PENDING),
        ConsumoSyncItem(3, "Tomada 10A", 5.0, "5 UN", "12/02/2026 14:20", "Instalação na cozinha", SyncStatus.ERROR)
    )
    SyncScreenContent(uiState = SyncUiState(consumos = sampleItems), onReenviarErros = {})
}
