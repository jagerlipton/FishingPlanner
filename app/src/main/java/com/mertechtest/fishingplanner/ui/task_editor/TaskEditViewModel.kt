package com.mertechtest.fishingplanner.ui.task_editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertechtest.fishingplanner.data.model.StateResult
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.data.model.WeatherResponse
import com.mertechtest.fishingplanner.data.repository.Repository
import com.mertechtest.fishingplanner.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class TaskEditViewModel(private val repository: Repository) : ViewModel() {

    private val _currentTaskLiveData =
        MutableLiveData<Event<StateResult<TaskDBmodel>>>()
    val currentTaskLiveData: LiveData<Event<StateResult<TaskDBmodel>>>
        get() = _currentTaskLiveData

    private val _weatherTaskLiveData =
        MutableLiveData<Event<StateResult<TaskDBmodel>>>()
    val weatherTaskLiveData: LiveData<Event<StateResult<TaskDBmodel>>>
        get() = _weatherTaskLiveData


    private lateinit var weatherResponse: WeatherResponse

    fun getWeatherAndTask(name: String, date: String, location: String, info: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse = repository.getWeatherFromNetwork(location, date)
                addWeatherTaskToDB(weatherResponse, name, date, info)
                _weatherTaskLiveData.postValue(Event(StateResult.success(TaskDBmodel())))

            } catch (e: IOException) {
                e.message?.let { Log.e("ololo", it) }
            }
        }
    }

    fun loadTaskFromDb(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.loadTaskById(id)
            withContext(Dispatchers.Main) {
                if (task != null) {
                    _currentTaskLiveData.value = Event(StateResult.success(task))
                }
            }
        }
    }

    private suspend fun addWeatherTaskToDB(
        weatherResponse: WeatherResponse,
        name: String,
        date: String,
        info: String
    ) {
        val newTask = TaskDBmodel()
        newTask.name = name
        newTask.icon = weatherResponse.weather.first().icon
        newTask.city = weatherResponse.name
        newTask.country = weatherResponse.sys.country
        newTask.temp = weatherResponse.main.temp
        newTask.info = info
        newTask.dateTime = date
        repository.addTaskToDB(newTask)
    }

      fun updateTaskToDB(id: Int, name: String, date: String, location: String, info: String){
         viewModelScope.launch(Dispatchers.IO) {
             val task = repository.loadTaskById(id.toInt())
             if (task != null) {
                 task.name = name
                 task.city = location
                 task.info = info
                 task.dateTime = date
                 repository.updateTaskFromDB(task)
                 _weatherTaskLiveData.postValue(Event(StateResult.success(TaskDBmodel())))
             }

         }
    }
}

