package com.mertechtest.fishingplanner.data.db

import androidx.room.*
import com.mertechtest.fishingplanner.data.model.TaskDBmodel


@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskDBmodel)

    @Query("DELETE FROM ${TaskDBmodel.TABLE_NAME} where id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("SELECT * FROM ${TaskDBmodel.TABLE_NAME} WHERE id = :id")
    suspend fun loadTaskById(id: Int): TaskDBmodel?

    @Query("SELECT * FROM ${TaskDBmodel.TABLE_NAME}")
    suspend fun loadAllTasks(): List<TaskDBmodel>

    @Update
    fun updateTask(task: TaskDBmodel?)


}