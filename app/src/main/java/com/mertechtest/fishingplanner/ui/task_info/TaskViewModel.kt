package com.mertechtest.fishingplanner.ui.task_info

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

class TaskViewModel(private val repository: Repository) : ViewModel() {

    private val _currentTaskLiveData =
        MutableLiveData<Event<StateResult<TaskDBmodel>>>()
    val currentTaskLiveData: LiveData<Event<StateResult<TaskDBmodel>>>
        get() = _currentTaskLiveData

    fun loadTaskFromDb(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.loadTaskById(id)
            if (task != null) {
                _currentTaskLiveData.postValue(Event(StateResult.success(task)))
            }
        }
    }

    fun completeTask(task: TaskDBmodel) {
        task.completed = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTaskToDB(task)
        }

    }

    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTaskById(id)
        }
    }
}