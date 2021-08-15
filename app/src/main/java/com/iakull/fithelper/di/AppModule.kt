package com.iakull.fithelper.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.WorkManager
import com.iakull.fithelper.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE) }
    single { WorkManager.getInstance(androidContext()) }
   // single { provideDb(androidContext()) }
}

/*
fun provideDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "FITHELPER.db")
            .createFromAsset("FITHELPER.db")
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
}*/
