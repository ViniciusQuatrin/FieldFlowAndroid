package br.com.satsolucoes.fieldflow.data.remote.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import br.com.satsolucoes.fieldflow.data.remote.model.ConsumoDto
import br.com.satsolucoes.fieldflow.data.remote.model.MaterialDto
import kotlinx.coroutines.delay
import retrofit2.Response
import java.io.IOException

/** Emulação do remoto. Simula latência e retorna dados baseados no mockup. */
class FakeMaterialApiService(private val context: Context) : MaterialApiService {

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override suspend fun fetchMaterials(): List<MaterialDto> {
        // Simula delay de rede
        delay(1500)

        if (!isOnline()) {
            throw IOException("Sem conexão com a internet")
        }

        return listOf(
            // Unidades contínuas (campo editável)
            MaterialDto(id = 1, nome = "Cabo Flexível 2.5mm", quantidadeDisponivel = 100.0, descricao = "Rolo de 100m - Preto", unidadeMedida = "M"),
            MaterialDto(id = 2, nome = "Cano PVC 50mm", quantidadeDisponivel = 25.5, descricao = "Tubo soldável esgoto", unidadeMedida = "M"),
            MaterialDto(id = 3, nome = "Areia Média", quantidadeDisponivel = 500.0, descricao = "Para construção", unidadeMedida = "KG"),
            MaterialDto(id = 4, nome = "Cimento CP-II", quantidadeDisponivel = 150.0, descricao = "Saco de 50kg", unidadeMedida = "KG"),
            MaterialDto(id = 5, nome = "Tinta Acrílica", quantidadeDisponivel = 36.0, descricao = "Branco Neve 18L", unidadeMedida = "L"),

            // Unidades discretas (botões +/-)
            MaterialDto(id = 6, nome = "Disjuntor 20A", quantidadeDisponivel = 15.0, descricao = "Unipolar Weg 220V", unidadeMedida = "UN"),
            MaterialDto(id = 7, nome = "Tomada 10A", quantidadeDisponivel = 12.0, descricao = "Conjunto 4x2 Branco", unidadeMedida = "UN"),
            MaterialDto(id = 8, nome = "Lâmpada LED 9W", quantidadeDisponivel = 30.0, descricao = "Branca Fria Bivolt", unidadeMedida = "UN"),
            MaterialDto(id = 9, nome = "Fita Isolante", quantidadeDisponivel = 20.0, descricao = "3M Imperial 18mm x 10m", unidadeMedida = "UN"),
            MaterialDto(id = 10, nome = "Luva PVC 25mm", quantidadeDisponivel = 50.0, descricao = "Soldável Tigre", unidadeMedida = "UN")
        )
    }

    override suspend fun postConsumo(consumo: ConsumoDto): Response<ConsumoDto> {
        // Simula delay de envio
        delay(1000)

        if (!isOnline()) {
            throw IOException("Sem conexão com a internet")
        }

        // Simula 90% de chance de sucesso
        return if ((0..9).random() != 0) {
            Response.success(consumo.copy(id = (1..1000).random().toLong()))
        } else {
            Response.error(500, okhttp3.ResponseBody.create(null, "Erro simulado"))
        }
    }
}