package com.iakull.fithelper.di

import com.iakull.fithelper.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single { ExerciseRepository(get()) }
    single { ProgramDayRepository(get(), get()) }
    single { ProgramDayExerciseRepository(get(), get()) }
    single { ProgramRepository(get(), get(), get(), get()) }
    single { TrainingExerciseRepository(get(), get()) }
    single { TrainingSetRepository(get(), get()) }
    single { TrainingRepository(get(), get()) }
}