package com.iakull.fithelper.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.iakull.fithelper.data.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @RawQuery(observedEntities = [Exercise::class])
    fun exercisesFlow(query: SupportSQLiteQuery): Flow<List<Exercise>>

    @Query("SELECT * FROM Exercise LIMIT :number")
    suspend fun exercises(number: Int): List<Exercise>

    @Query("SELECT * FROM Exercise WHERE name = :name")
    suspend fun exercise(name: String): Exercise

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(program: Exercise)

    @Query("DELETE FROM Exercise WHERE name = :name")
    suspend fun delete(name: String)

    @Query("UPDATE Exercise SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun setFavorite(name: String, isFavorite: Boolean)

    @Query("UPDATE Exercise SET executionsCount = executionsCount + 1 WHERE name = :name")
    suspend fun increaseExecutionsCnt(name: String)

    @Query("UPDATE Exercise SET executionsCount = executionsCount - 1 WHERE name = :name")
    suspend fun decreaseExecutionsCnt(name: String)
}