package com.mertechtest.fishingplanner.ui.main_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertechtest.fishingplanner.data.model.StateResult
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.data.repository.Repository
import com.mertechtest.fishingplanner.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _taskListLiveData =
        MutableLiveData<Event<StateResult<List<TaskDBmodel>>>>()
    val taskListLiveData: LiveData<Event<StateResult<List<TaskDBmodel>>>>
        get() = _taskListLiveData

    fun loadAllTasksFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val taskList = repository.loadAllTasksFromDB()
            Log.d("ololo", taskList.toString())
            withContext(Dispatchers.Main) {
                _taskListLiveData.postValue(Event(StateResult.success(taskList)))
            }
        }
    }

    fun refreshAllTasksFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val taskList = repository.loadAllTasksFromDB()
            taskList.forEach {
                if (it.city != null && it.dateTime != null) repository.getWeatherFromNetwork(
                    it.city!!,
                    it.dateTime!!
                )
            }
        }
    }
}