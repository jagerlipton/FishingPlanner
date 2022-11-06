package com.mertechtest.fishingplanner

import android.app.Application
import com.mertechtest.fishingplanner.di.AppComponent
import com.mertechtest.fishingplanner.di.AppModule
import com.mertechtest.fishingplanner.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

    }


}

