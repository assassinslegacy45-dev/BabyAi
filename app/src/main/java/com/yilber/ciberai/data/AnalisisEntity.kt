package com.yilber.ciberai.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "analisis")
data class AnalisisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val comando: String,
    val salida: String,
    val analisis: String,
    val siguientesPasos: String,
    val creadoEn: Long = Instant.now().toEpochMilli()
)
