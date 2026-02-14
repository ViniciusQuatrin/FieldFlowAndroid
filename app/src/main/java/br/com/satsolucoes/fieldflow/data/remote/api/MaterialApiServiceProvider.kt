package br.com.satsolucoes.fieldflow.data.remote.api

import android.content.Context
import br.com.satsolucoes.fieldflow.data.remote.config.ApiConfig
import br.com.satsolucoes.fieldflow.data.remote.config.ApiMode
import br.com.satsolucoes.fieldflow.data.remote.model.ConsumoDto
import br.com.satsolucoes.fieldflow.data.remote.model.MaterialDto
import retrofit2.Response

/**
 * Provider que delega chamadas para FakeApi ou API real baseado na configuração.
 * Implementa o padrão Strategy para alternar entre implementações em runtime.
 */
class MaterialApiServiceProvider(
    private val context: Context,
    private val retrofitApiService: MaterialApiService
) : MaterialApiService {

    private val fakeApiService: FakeMaterialApiService by lazy {
        FakeMaterialApiService(context)
    }

    private fun getCurrentService(): MaterialApiService {
        val currentMode = ApiConfig.apiMode.value
        return when (currentMode) {
            ApiMode.FAKE -> {
                fakeApiService
            }
            ApiMode.SERVER -> {
                retrofitApiService
            }
        }
    }

    override suspend fun fetchMaterials(): List<MaterialDto> {
        return getCurrentService().fetchMaterials()
    }

    override suspend fun postConsumo(consumo: ConsumoDto): Response<ConsumoDto> {
        return getCurrentService().postConsumo(consumo)
    }
}
