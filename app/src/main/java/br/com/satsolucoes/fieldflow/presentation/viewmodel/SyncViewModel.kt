package br.com.satsolucoes.fieldflow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.domain.repository.ConsumoRepository
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class SyncViewModel(
    private val consumoRepository: ConsumoRepository,
    private val materialRepository: MaterialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SyncUiState(isLoading = true))
    val uiState: StateFlow<SyncUiState> = _uiState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    init {
        carregarConsumos()
    }

    private fun carregarConsumos() {
        viewModelScope.launch {
            consumoRepository.obterTodosConsumos()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { consumos ->
                    val items = consumos.map { consumo ->
                        val material = materialRepository.obterMaterialPorId(consumo.materialId)
                        ConsumoSyncItem(
                            id = consumo.id,
                            materialNome = material?.nome ?: "Material #${consumo.materialId}",
                            quantidade = consumo.quantidadeConsumida,
                            unidadeFormatada = material?.unidadeMedida?.formatarQuantidade(consumo.quantidadeConsumida)
                                ?: "${consumo.quantidadeConsumida}",
                            dataFormatada = consumo.data.format(dateFormatter),
                            observacao = consumo.observacao,
                            status = consumo.statusSync
                        )
                    }
                    _uiState.update {
                        it.copy(consumos = items, isLoading = false, error = null)
                    }
                }
        }
    }

    fun reenviarComErro() {
        viewModelScope.launch {
            _uiState.value.consumos
                .filter { it.status == SyncStatus.ERROR }
                .forEach { item ->
                    consumoRepository.atualizarStatusSync(item.id, SyncStatus.PENDING)
                }
        }
    }
}

