package com.baby.ai.data

import androidx.room.*

@Dao
interface TrainingDao {
    @Insert
    suspend fun insert(entry: DataEntry)

    @Query("SELECT * FROM data_entries")
    suspend fun getAll(): List<DataEntry>

    @Query("DELETE FROM data_entries")
    suspend fun clearAll()
}