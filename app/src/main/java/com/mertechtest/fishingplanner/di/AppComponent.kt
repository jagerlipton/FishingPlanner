package com.mertechtest.fishingplanner.di

import com.mertechtest.fishingplanner.ui.main_screen.MainFragment
import com.mertechtest.fishingplanner.ui.task_editor.TaskEditFragment
import com.mertechtest.fishingplanner.ui.task_info.TaskFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class]
)

@Singleton
interface AppComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(taskEditFragment: TaskEditFragment)
    fun inject(taskFragment: TaskFragment)
}