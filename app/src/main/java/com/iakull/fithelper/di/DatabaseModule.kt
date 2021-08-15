package com.iakull.fithelper.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.iakull.fithelper.R
import com.iakull.fithelper.data.local.AppDatabase
import com.iakull.fithelper.data.remote.data_sources.ImageSource
import com.iakull.fithelper.data.remote.data_sources.PostSource
import com.iakull.fithelper.data.remote.data_sources.ProgramSource
import com.iakull.fithelper.data.remote.data_sources.TrainingSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    // Data Access Objects
    single { get<AppDatabase>().exerciseDao() }
    single { get<AppDatabase>().muscleDao() }
    single { get<AppDatabase>().programDao() }
    single { get<AppDatabase>().programDayDao() }
    single { get<AppDatabase>().programDayExerciseDao() }
    single { get<AppDatabase>().targetedMuscleDao() }
    single { get<AppDatabase>().trainingDao() }
    single { get<AppDatabase>().trainingExerciseDao() }
    single { get<AppDatabase>().trainingSetDao() }

    single {
        Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                androidApplication().baseContext.getString(R.string.app_name)
        )
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .build()
    }

    // Remote data sources
    single { ImageSource() }
    single { PostSource() }
    single { ProgramSource(get()) }
    single { TrainingSource(get()) }
}