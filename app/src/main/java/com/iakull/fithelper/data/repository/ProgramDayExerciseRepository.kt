package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.PlanDayExerciseDao
import com.iakull.fithelper.data.model.ProgramDayExercise
import com.iakull.fithelper.data.remote.data_sources.ProgramSource
import com.iakull.fithelper.workers.UploadProgramDayExerciseListWorker
import com.iakull.fithelper.workers.UploadProgramDayExerciseWorker

class ProgramDayExerciseRepository(
        private val dao: PlanDayExerciseDao,
        private val src: ProgramSource
) {

    fun programDayExercisesFlow(programDayId: String) = dao.programDayExercisesFlow(programDayId)

    suspend fun programDayExercises(programDayId: String) = dao.programDayExercises(programDayId)

    suspend fun programDayExercise(id: String) = dao.programDayExercise(id)

    suspend fun publicProgramDayExercises(programId: String, programDayId: String) =
            src.programDayExercises(programId, programDayId)

    suspend fun insert(programDayExercise: ProgramDayExercise) {
        dao.insert(programDayExercise)
        src.scheduleUpload(programDayExercise.id, UploadProgramDayExerciseWorker::class.java)
    }

    suspend fun update(programDayExercises: List<ProgramDayExercise>) {
        if (programDayExercises.isEmpty()) return

        dao.update(programDayExercises)
        src.scheduleUpload(programDayExercises[0].programDayId, UploadProgramDayExerciseListWorker::class.java)
    }

    suspend fun delete(id: String) {
        dao.delete(id)
        src.scheduleDeletion(id, UploadProgramDayExerciseWorker::class.java)
    }

    fun uploadProgramDayExercise(programDayExercise: ProgramDayExercise) {
        src.uploadProgramDayExercise(programDayExercise)
    }

    fun uploadProgramDayExercises(programDayExercises: List<ProgramDayExercise>) {
        programDayExercises.forEach { src.uploadProgramDayExercise(it) }
    }

    suspend fun syncProgramDayExercises(lastUpdate: Long) {
        val items = src.newProgramDayExercises(lastUpdate)
        val (deletedItems, existingItems) = items.partition { it.deleted }

        deletedItems.forEach { dao.delete(it.id) }
        dao.insert(existingItems)
    }
}