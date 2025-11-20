package com.example.bookreadingtracker

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val req = PeriodicWorkRequestBuilder<com.example.bookreadingtracker.core.ShelfRefreshWorker>(1, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "shelf_refresh",
            ExistingPeriodicWorkPolicy.KEEP,
            req
        )
    }
}
