package com.mertechtest.fishingplanner.di

import com.mertechtest.fishingplanner.data.db.TaskDB
import com.mertechtest.fishingplanner.data.network.Interceptor
import com.mertechtest.fishingplanner.data.network.NetworkAPI
import com.mertechtest.fishingplanner.data.repository.Repository
import com.mertechtest.fishingplanner.ui.main_screen.MainViewModel
import com.mertechtest.fishingplanner.ui.task_editor.TaskEditViewModel
import com.mertechtest.fishingplanner.ui.task_info.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Repository> {
        Repository(
            api = get(),
            db = get()
        )
    }

    single {
        NetworkAPI.invoke(Interceptor(context = get()))
    }

    single {
       TaskDB.invoke(context = get())
    }

    viewModel {
        MainViewModel(
            repository = get()
        )
    }

    viewModel {
        TaskEditViewModel(
            repository = get()
        )
    }

    viewModel {
        TaskViewModel(
            repository = get()
        )
    }
}