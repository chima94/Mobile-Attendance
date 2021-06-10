package com.example.smartattendancesystem

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}