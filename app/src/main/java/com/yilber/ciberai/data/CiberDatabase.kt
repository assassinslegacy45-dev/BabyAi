package com.yilber.ciberai.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AnalisisEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CiberDatabase : RoomDatabase() {

    abstract fun analisisDao(): AnalisisDao

    companion object {
        @Volatile
        private var INSTANCE: CiberDatabase? = null

        fun getInstance(context: Context): CiberDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CiberDatabase::class.java,
                    "ciber_ai.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
