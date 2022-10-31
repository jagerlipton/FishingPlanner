package com.mertechtest.fishingplanner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mertechtest.fishingplanner.data.model.TaskDBmodel

@Database(
    entities = [TaskDBmodel::class],
    version = 1
)

abstract class TaskDB : RoomDatabase() {

    abstract fun getDao(): TaskDAO

    companion object {
        const val DB_NAME = "fishing_database"

        @Volatile
        private var INSTANCE: TaskDB? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TaskDB::class.java,
                DB_NAME
            ).build()
    }
}