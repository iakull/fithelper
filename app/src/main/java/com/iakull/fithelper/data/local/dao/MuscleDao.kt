package com.iakull.fithelper.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.iakull.fithelper.data.model.Muscle
import com.iakull.fithelper.data.relation.FilterParam
import kotlinx.coroutines.flow.Flow

@Dao
interface MuscleDao {

    @RawQuery(observedEntities = [Muscle::class])
    fun musclesFlow(query: SupportSQLiteQuery): Flow<List<Muscle>>

    @Query("SELECT * FROM Muscle LIMIT :number")
    suspend fun exercises(number: Int): List<Muscle>

    @Query("SELECT * FROM Muscle WHERE name = :name")
    suspend fun exercise(name: String): Muscle

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercises: List<Muscle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(program: Muscle)

    @Query("DELETE FROM Muscle WHERE name = :name")
    suspend fun delete(name: String)

    @Query("SELECT name, 0 AS isActive FROM Muscle")
    suspend fun params() : List<FilterParam>



}