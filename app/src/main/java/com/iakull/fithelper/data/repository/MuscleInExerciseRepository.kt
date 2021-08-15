package com.iakull.fithelper.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.iakull.fithelper.data.local.dao.ExerciseDao
import com.iakull.fithelper.data.local.dao.MuscleInExerciseDao
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.TargetedMuscle

class MuscleInExerciseRepository(private val daoExercise: MuscleInExerciseDao) {

    /*fun exercisesFlow(query: SupportSQLiteQuery) = daoExercise.exercisesFlow(query)
    suspend fun exercises(number: Int) = daoExercise.exercises(number)
    suspend fun exercise(name: String) = daoExercise.exercise(name)*/

    suspend fun insertMuscleInExercise(exr: TargetedMuscle) {
        daoExercise.insertItem(exr)
    }
}
