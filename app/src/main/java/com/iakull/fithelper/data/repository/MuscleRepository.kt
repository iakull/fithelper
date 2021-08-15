package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.PlanDao
import com.iakull.fithelper.data.local.dao.PlanDayDao
import com.iakull.fithelper.data.local.dao.PlanDayExerciseDao
import com.iakull.fithelper.data.remote.data_sources.ProgramSource

class MuscleRepository(
        private val planDao: PlanDao,
        private val planDayDao: PlanDayDao,
        private val planDayExerciseDao: PlanDayExerciseDao,
        private val src: ProgramSource
) {

    fun programsFlow() = planDao.programsFlow()

    suspend fun program(id: String) = planDao.program(id)

    fun publicPrograms() = src.programs()

    fun publicPrograms(limit: Long) = src.programs(limit)

    suspend fun publicProgram(id: String) = src.program(id)

}