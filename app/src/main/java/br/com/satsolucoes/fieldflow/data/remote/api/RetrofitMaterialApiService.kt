package br.com.satsolucoes.fieldflow.data.remote.api

import br.com.satsolucoes.fieldflow.data.remote.config.ApiConfig
import br.com.satsolucoes.fieldflow.data.remote.model.ConsumoDto
import br.com.satsolucoes.fieldflow.data.remote.model.MaterialDto
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Fábrica que cria instâncias do MaterialApiService via Retrofit.
 * Recria o Retrofit a cada chamada para usar a URL mais recente do ApiConfig.
 */
class RetrofitMaterialApiService(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) : MaterialApiService {

    private fun createService(): MaterialApiService {
        val baseUrl = ApiConfig.serverUrl.value

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(MaterialApiService::class.java)
    }

    override suspend fun fetchMaterials(): List<MaterialDto> {
        val result = createService().fetchMaterials()
        return result
    }

    override suspend fun postConsumo(consumo: ConsumoDto): Response<ConsumoDto> {
        return createService().postConsumo(consumo)
    }
}
