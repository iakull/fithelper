package com.iakull.fithelper.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.iakull.fithelper.data.local.dao.ExerciseDao
import com.iakull.fithelper.data.model.Exercise

class ExerciseRepository(private val daoExercise: ExerciseDao) {

    fun exercisesFlow(query: SupportSQLiteQuery) = daoExercise.exercisesFlow(query)
    suspend fun exercises(number: Int) = daoExercise.exercises(number)
    suspend fun exercise(name: String) = daoExercise.exercise(name)
    suspend fun setFavorite(name: String, isFavorite: Boolean) = daoExercise.setFavorite(name, isFavorite)
    suspend fun increaseExecutionsCnt(name: String) = daoExercise.increaseExecutionsCnt(name)
    suspend fun decreaseExecutionsCnt(name: String) = daoExercise.decreaseExecutionsCnt(name)

    suspend fun insertExercise(exr: Exercise) {
        daoExercise.insertItem(exr)
    }

    suspend fun delete(name: String) {
        daoExercise.delete(name)
    }
}
