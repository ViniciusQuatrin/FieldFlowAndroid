package br.com.satsolucoes.fieldflow.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.satsolucoes.fieldflow.data.repository.MaterialRepositoryImpl
import br.com.satsolucoes.fieldflow.data.worker.SyncScheduler
import br.com.satsolucoes.fieldflow.domain.enums.SyncStatus
import br.com.satsolucoes.fieldflow.domain.model.Consumo
import br.com.satsolucoes.fieldflow.domain.repository.ConsumoRepository
import br.com.satsolucoes.fieldflow.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MaterialViewModel(
    private val materialRepository: MaterialRepository,
    private val consumoRepository: ConsumoRepository,
    private val applicationContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MaterialUiState(isLoading = true))
    val uiState: StateFlow<MaterialUiState> = _uiState.asStateFlow()

    private val _quantidadesConsumo = MutableStateFlow<Map<Int, Double>>(emptyMap())
    private val _observacoes = MutableStateFlow<Map<Int, String>>(emptyMap())

    init {
        inicializarECarregar()
    }

    private fun inicializarECarregar() {
        viewModelScope.launch {
            (materialRepository as? MaterialRepositoryImpl)?.sincronizarMateriais()

            carregarMateriais()
        }
    }

    private fun carregarMateriais() {
        viewModelScope.launch {
            combine(
                materialRepository.obterTodosMateriais(),
                _quantidadesConsumo,
                _observacoes
            ) { materiais, quantidades, observacoes ->
                materiais.map { material ->
                    MaterialItemState(
                        material = material,
                        quantidadeConsumo = quantidades[material.id] ?: 0.0,
                        observacao = observacoes[material.id] ?: ""
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect { items ->
                _uiState.update {
                    it.copy(materiais = items, isLoading = false, error = null)
                }
            }
        }
    }

    /** Para unidades discretas (UN) - incrementa em 1 */
    fun incrementarConsumo(materialId: Int) {
        _quantidadesConsumo.update { current ->
            val quantidade = current[materialId] ?: 0.0
            current + (materialId to quantidade + 1.0)
        }
    }

    /** Para unidades discretas (UN) - decrementa em 1 */
    fun decrementarConsumo(materialId: Int) {
        _quantidadesConsumo.update { current ->
            val quantidade = current[materialId] ?: 0.0
            if (quantidade > 0) {
                current + (materialId to quantidade - 1.0)
            } else {
                current
            }
        }
    }

    /** Para unidades contínuas (KG, M, L) - seta valor diretamente */
    fun setarQuantidadeConsumo(materialId: Int, quantidade: Double) {
        val material = _uiState.value.materiais.find { it.id == materialId } ?: return
        val quantidadeValida = quantidade.coerceIn(0.0, material.quantidadeDisponivel)

        _quantidadesConsumo.update { current ->
            current + (materialId to quantidadeValida)
        }
    }

    /** Atualiza a observação de um material */
    fun setarObservacao(materialId: Int, observacao: String) {
        _observacoes.update { current ->
            current + (materialId to observacao)
        }
    }

    private suspend fun registrarConsumo(materialId: Int, quantidade: Double, observacao: String) {
        val consumo = Consumo(
            materialId = materialId,
            quantidadeConsumida = quantidade,
            statusSync = SyncStatus.PENDING,
            observacao = observacao,
            data = LocalDateTime.now()
        )

        consumoRepository.registrarConsumo(consumo)
        materialRepository.consumirEstoque(materialId, quantidade)

        // Reset quantidade e observação
        _quantidadesConsumo.update { current ->
            current + (materialId to 0.0)
        }
        _observacoes.update { current ->
            current + (materialId to "")
        }
    }

    fun confirmarTodosConsumos() {
        viewModelScope.launch {
            val consumosParaRegistrar = _quantidadesConsumo.value.filter { it.value > 0 }

            consumosParaRegistrar.forEach { (materialId, quantidade) ->
                val observacao = _observacoes.value[materialId] ?: ""
                registrarConsumo(materialId, quantidade, observacao)
            }

            // Trigger sincronização imediata após registrar consumos
            if (consumosParaRegistrar.isNotEmpty()) {
                SyncScheduler.agendarSincronizacaoImediata(applicationContext)
            }
        }
    }

    /**
     * Força a ressincronização dos materiais com a fonte de dados atual (Fake ou Servidor).
     * Chamado quando o usuário altera o modo de API.
     * Limpa os dados antigos e busca novos da fonte selecionada.
     */
    fun forcarRessincronizacao() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val resultado = (materialRepository as? MaterialRepositoryImpl)?.limparERessincronizar()
                if (resultado?.isFailure == true) {
                    val erro = resultado.exceptionOrNull()?.message ?: "Erro desconhecido"
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao sincronizar: $erro") }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao sincronizar: ${e.message}") }
            }
        }
    }
}

