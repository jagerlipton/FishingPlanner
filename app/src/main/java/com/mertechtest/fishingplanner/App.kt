package com.mertechtest.fishingplanner

import android.app.Application
import com.mertechtest.fishingplanner.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(appModule))
            androidContext(this@App)
        }
    }


}

