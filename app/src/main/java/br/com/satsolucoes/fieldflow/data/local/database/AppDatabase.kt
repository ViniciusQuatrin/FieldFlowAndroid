package br.com.satsolucoes.fieldflow.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.satsolucoes.fieldflow.data.local.dao.ConsumoDao
import br.com.satsolucoes.fieldflow.data.local.dao.MaterialDao
import br.com.satsolucoes.fieldflow.data.local.entity.ConsumoEntity
import br.com.satsolucoes.fieldflow.data.local.entity.MaterialEntity

@Database(
    entities = [
        MaterialEntity::class,
        ConsumoEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materialDao(): MaterialDao
    abstract fun consumoDao(): ConsumoDao

    companion object {
        const val DATABASE_NAME = "fieldflow_database"
    }
}

