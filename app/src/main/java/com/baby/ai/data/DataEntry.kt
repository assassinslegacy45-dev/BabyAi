package com.baby.ai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_entries")
data class DataEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val input: String,
    val output: String
)