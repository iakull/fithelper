package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.PlanDao
import com.iakull.fithelper.data.local.dao.PlanDayDao
import com.iakull.fithelper.data.local.dao.PlanDayExerciseDao
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.remote.data_sources.ProgramSource
import com.iakull.fithelper.workers.UploadProgramWorker

class ProgramRepository(
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

    suspend fun insert(program: Program) {
        planDao.insert(program)
        src.scheduleUpload(program.id, UploadProgramWorker::class.java)
    }

    suspend fun delete(id: String) {
        planDao.delete(id)
        src.scheduleDeletion(id, UploadProgramWorker::class.java)
    }

    suspend fun publishProgram(program: Program) {
        src.publishProgram(program)
        planDayDao.programDays(program.id).forEach { programDay ->
            src.publishProgramDay(program.id, programDay)
            planDayExerciseDao.programDayExercises(programDay.id).forEach { exercise ->
                src.publishProgramDayExercise(program.id, programDay.id, exercise)
            }
        }
    }

    suspend fun addToMyPrograms(program: Program) {
        planDao.insert(program)
        val programDays = src.programDays(program.id)
        planDayDao.insert(programDays)
        for (programDay in programDays) {
            val exercises = src.programDayExercises(program.id, programDay.id)
            planDayExerciseDao.insert(exercises)
        }
    }

    fun uploadProgram(program: Program) {
        src.uploadProgram(program)
    }

    suspend fun syncPrograms(lastUpdate: Long) {
        val items = src.newPrograms(lastUpdate)
        val (deletedItems, existingItems) = items.partition { it.deleted }

        deletedItems.forEach { planDao.delete(it.id) }
        planDao.insert(existingItems)
    }
}