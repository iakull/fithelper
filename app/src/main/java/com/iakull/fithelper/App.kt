package com.iakull.fithelper

import android.app.Application
import com.iakull.fithelper.di.appModule
import com.iakull.fithelper.di.databaseModule
import com.iakull.fithelper.di.repositoryModule
import com.iakull.fithelper.di.viewModelsModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

const val AUTHORITY = "com.iakull.fithelper"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
            modules(databaseModule)
            modules(viewModelsModule)
            modules(repositoryModule)
        }

        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}