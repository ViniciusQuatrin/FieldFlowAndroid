package br.com.satsolucoes.fieldflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.satsolucoes.fieldflow.data.local.entity.ConsumoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(consumo: ConsumoEntity)

    @Update
    suspend fun atualizar(consumo: ConsumoEntity)

    @Delete
    suspend fun deletar(consumo: ConsumoEntity)

    @Query("SELECT * FROM consumos WHERE id = :id")
    suspend fun obterPorId(id: Int): ConsumoEntity?

    @Query("SELECT * FROM consumos ORDER BY data DESC")
    fun obterTodos(): Flow<List<ConsumoEntity>>

    @Query("SELECT * FROM consumos WHERE materialId = :materialId ORDER BY data DESC")
    fun obterPorMaterial(materialId: Int): Flow<List<ConsumoEntity>>

    @Query("SELECT * FROM consumos WHERE statusSync = 'PENDING' ORDER BY data ASC")
    fun obterPendentes(): Flow<List<ConsumoEntity>>

    @Query("SELECT * FROM consumos WHERE statusSync = 'PENDING' ORDER BY data ASC")
    suspend fun obterPendentesLista(): List<ConsumoEntity>

    @Query("UPDATE consumos SET statusSync = :status WHERE id = :consumoId")
    suspend fun atualizarStatusSync(consumoId: Int, status: String)
}

