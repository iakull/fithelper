package com.iakull.fithelper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iakull.fithelper.data.local.dao.*
import com.iakull.fithelper.data.model.*

@Database(
        entities = [
            Muscle::class,
            Exercise::class,
            Program::class,
            ProgramDay::class,
            ProgramDayExercise::class,
            TargetedMuscle::class,
            Training::class,
            TrainingExercise::class,
            TrainingSet::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleDao(): MuscleDao
    abstract fun programDao(): PlanDao
    abstract fun programDayDao(): PlanDayDao
    abstract fun programDayExerciseDao(): PlanDayExerciseDao
    abstract fun targetedMuscleDao(): MuscleInExerciseDao
    abstract fun trainingDao(): TrainingDao
    abstract fun trainingExerciseDao(): TrainingExerciseDao
    abstract fun trainingSetDao(): TrainingSetDao
}