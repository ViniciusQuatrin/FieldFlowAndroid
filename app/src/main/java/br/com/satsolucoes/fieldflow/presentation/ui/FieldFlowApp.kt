package br.com.satsolucoes.fieldflow.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.satsolucoes.fieldflow.presentation.ui.components.BottomNavBar
import br.com.satsolucoes.fieldflow.presentation.ui.components.BottomNavItem
import br.com.satsolucoes.fieldflow.presentation.ui.screens.materials.MaterialsScreen
import br.com.satsolucoes.fieldflow.presentation.ui.screens.sync.SyncScreen
import br.com.satsolucoes.fieldflow.presentation.ui.theme.FieldFlowTheme
import br.com.satsolucoes.fieldflow.presentation.viewmodel.MaterialViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FieldFlowApp() {
    FieldFlowTheme {
        var selectedTab by remember { mutableStateOf(BottomNavItem.MATERIALS) }

        // ViewModel compartilhado para acessar estado de consumos pendentes
        val materialViewModel: MaterialViewModel = koinViewModel()
        val materialUiState by materialViewModel.uiState.collectAsState()

        val temConsumosPendentes = materialUiState.materiais.any { it.quantidadeConsumo > 0 }
        val mostrarFab = selectedTab == BottomNavItem.MATERIALS && temConsumosPendentes

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    selectedItem = selectedTab,
                    onItemSelected = { selectedTab = it },
                    showFab = mostrarFab,
                    onFabClick = materialViewModel::confirmarTodosConsumos
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    BottomNavItem.MATERIALS -> MaterialsScreen(viewModel = materialViewModel)
                    BottomNavItem.SYNC -> SyncScreen()
                }
            }
        }
    }
}


