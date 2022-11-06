package com.mertechtest.fishingplanner.di

import android.content.Context
import com.mertechtest.fishingplanner.data.db.TaskDB
import com.mertechtest.fishingplanner.data.network.Interceptor
import com.mertechtest.fishingplanner.data.network.NetworkAPI
import com.mertechtest.fishingplanner.data.repository.Repository
import com.mertechtest.fishingplanner.ui.main_screen.MainViewModelFactory
import com.mertechtest.fishingplanner.ui.task_editor.TaskEditViewModelFactory
import com.mertechtest.fishingplanner.ui.task_info.TaskViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context = context


    @Provides
    @Singleton
    fun provideRepository(api: NetworkAPI, db: TaskDB): Repository {
        return Repository(api, db)
    }

    @Provides
    @Singleton
    fun providesRoomDatabase(context: Context): TaskDB {
        return  TaskDB.invoke(context)
    }

    @Provides
    @Singleton
    fun providesNetworkAPI(context: Context): NetworkAPI {
        return   NetworkAPI.invoke(Interceptor(context))

    }

    @Provides
    fun provideMainViewModelFactory(repository: Repository): MainViewModelFactory {
        return MainViewModelFactory(repository = repository)
    }

    @Provides
    fun provideTaskEditViewModelFactory(repository: Repository): TaskEditViewModelFactory {
        return TaskEditViewModelFactory(repository = repository)
    }

    @Provides
    fun provideTaskViewModelFactory(repository: Repository): TaskViewModelFactory {
        return TaskViewModelFactory(repository = repository)
    }
}