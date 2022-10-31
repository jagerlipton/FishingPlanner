package com.mertechtest.fishingplanner.data.repository

import com.mertechtest.fishingplanner.data.db.TaskDB
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.data.model.WeatherResponse
import com.mertechtest.fishingplanner.data.network.NetworkAPI
import com.mertechtest.fishingplanner.data.network.Request

class Repository(
    private val api: NetworkAPI,
    private val db: TaskDB
) : Request() {

    suspend fun getWeatherFromNetwork(cityName: String, date: String): WeatherResponse =
        apiRequest {
            api.getWeather(cityName, date)
        }

    suspend fun addTaskToDB(newTask: TaskDBmodel) {
        db.getDao().addTask(newTask)
    }

    suspend fun deleteTaskById(id: Int) {
        db.getDao().deleteTaskById(id)
    }

    suspend fun loadTaskById(id: Int): TaskDBmodel? =
        db.getDao().loadTaskById(id)

    suspend fun loadAllTasksFromDB(): List<TaskDBmodel> =
        db.getDao().loadAllTasks()

    suspend fun updateTaskFromDB(task: TaskDBmodel) =
        db.getDao().updateTask(task)
}
