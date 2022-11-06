package com.mertechtest.fishingplanner.ui.task_editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertechtest.fishingplanner.data.repository.Repository


@Suppress("UNCHECKED_CAST")
class TaskEditViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskEditViewModel(repository) as T
    }
}