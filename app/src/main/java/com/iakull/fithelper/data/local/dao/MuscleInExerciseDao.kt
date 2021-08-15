package com.iakull.fithelper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.TargetedMuscle
import com.iakull.fithelper.data.relation.FilterParam

@Dao
interface MuscleInExerciseDao {

    @Query("SELECT muscle FROM TargetedMuscle WHERE exercise = :exerciseName")
    suspend fun targetedMuscles(exerciseName: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(targetedMuscles: List<TargetedMuscle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(program: TargetedMuscle)
}