package br.com.satsolucoes.fieldflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.satsolucoes.fieldflow.data.local.entity.MaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(material: MaterialEntity)

    @Update
    suspend fun atualizar(material: MaterialEntity)

    @Delete
    suspend fun deletar(material: MaterialEntity)

    @Query("SELECT * FROM materials WHERE id = :id")
    suspend fun obterPorId(id: Int): MaterialEntity?

    @Query("SELECT * FROM materials ORDER BY nome ASC")
    fun obterTodos(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE quantidadeDisponivel <= :limite ORDER BY quantidadeDisponivel ASC")
    fun obterComEstoqueBaixo(limite: Double): Flow<List<MaterialEntity>>

    @Query("UPDATE materials SET quantidadeDisponivel = :novaQuantidade WHERE id = :materialId")
    suspend fun atualizarEstoque(materialId: Int, novaQuantidade: Double)

    @Query("DELETE FROM materials")
    suspend fun deletarTodos()
}

