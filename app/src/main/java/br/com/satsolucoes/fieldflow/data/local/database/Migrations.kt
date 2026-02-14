package br.com.satsolucoes.fieldflow.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migrações do banco de dados.
 */
object Migrations {

    /**
     * Migração 1 → 2
     * Adiciona campo 'unidadeMedida' na tabela materials
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE materials ADD COLUMN unidadeMedida TEXT NOT NULL DEFAULT 'UN'")
        }
    }

    /**
     * Migração 2 → 3
     * Adiciona campo 'observacao' na tabela consumos
     */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE consumos ADD COLUMN observacao TEXT NOT NULL DEFAULT ''")
        }
    }

    /**
     * Migração 3 → 4
     * Altera campo 'descricao' na tabela materials para aceitar valores NULL
     * SQLite não suporta ALTER COLUMN, então precisamos recriar a tabela
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 1. Criar tabela temporária com a nova estrutura (descricao nullable)
            db.execSQL("""
                CREATE TABLE materials_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    nome TEXT NOT NULL,
                    quantidadeDisponivel REAL NOT NULL,
                    descricao TEXT,
                    unidadeMedida TEXT NOT NULL DEFAULT 'UN'
                )
            """.trimIndent())

            // 2. Copiar dados da tabela antiga para a nova
            db.execSQL("""
                INSERT INTO materials_temp (id, nome, quantidadeDisponivel, descricao, unidadeMedida)
                SELECT id, nome, quantidadeDisponivel, descricao, unidadeMedida FROM materials
            """.trimIndent())

            // 3. Remover tabela antiga
            db.execSQL("DROP TABLE materials")

            // 4. Renomear tabela temporária para o nome original
            db.execSQL("ALTER TABLE materials_temp RENAME TO materials")
        }
    }

    /**
     * Lista de todas as migrações para uso no Room.databaseBuilder
     */
    val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
}

