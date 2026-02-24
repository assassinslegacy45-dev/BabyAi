package com.yilber.ciberai.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalisisDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(analisis: AnalisisEntity)

    @Query("SELECT * FROM analisis ORDER BY creadoEn DESC")
    fun obtenerTodos(): Flow<List<AnalisisEntity>>

    @Query(
        "DELETE FROM analisis WHERE id NOT IN (" +
                "SELECT id FROM analisis ORDER BY creadoEn DESC LIMIT :maxFilas" +
                ")"
    )
    suspend fun borrarAntiguosManteniendo(maxFilas: Int)
}
