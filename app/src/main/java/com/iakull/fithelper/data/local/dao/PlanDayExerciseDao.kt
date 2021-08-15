package com.iakull.fithelper.data.local.dao

import androidx.room.*
import com.iakull.fithelper.data.model.ProgramDayExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDayExerciseDao {

    @Query("""
        SELECT * FROM ProgramDayExercise
        WHERE programDayId = :programDayId
        ORDER BY indexNumber
    """)
    fun programDayExercisesFlow(programDayId: String): Flow<List<ProgramDayExercise>>

    @Query("SELECT * FROM ProgramDayExercise WHERE programDayId = :programDayId")
    suspend fun programDayExercises(programDayId: String): List<ProgramDayExercise>

    @Query("SELECT * FROM ProgramDayExercise WHERE id = :id")
    suspend fun programDayExercise(id: String): ProgramDayExercise


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(programDayExercises: List<ProgramDayExercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(programDayExercise: ProgramDayExercise)


    @Update
    suspend fun update(programDayExercises: List<ProgramDayExercise>)


    @Query("DELETE FROM ProgramDayExercise WHERE id = :id")
    suspend fun delete(id: String)
}