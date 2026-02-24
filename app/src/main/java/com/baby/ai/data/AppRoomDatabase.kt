package com.baby.ai.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataEntry::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun trainingDao(): TrainingDao
}