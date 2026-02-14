package br.com.satsolucoes.fieldflow.data.remote.api

import br.com.satsolucoes.fieldflow.data.remote.model.ConsumoDto
import br.com.satsolucoes.fieldflow.data.remote.model.MaterialDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MaterialApiService {
    @GET("api/materiais/todos")
    suspend fun fetchMaterials(): List<MaterialDto>

    @POST("api/movimentacoes")
    suspend fun postConsumo(@Body consumo: ConsumoDto): Response<ConsumoDto>
}
