package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.PlanDayDao
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.remote.data_sources.ProgramSource
import com.iakull.fithelper.workers.UploadProgramDayListWorker
import com.iakull.fithelper.workers.UploadProgramDayWorker

class ProgramDayRepository(
        private val dao: PlanDayDao,
        private val src: ProgramSource
) {

    fun programDaysFlow(programId: String) = dao.programDaysFlow(programId)

    suspend fun programDays(programId: String) = dao.programDays(programId)

    suspend fun publicProgramDay(programId: String, programDayId: String) =
            src.programDay(programId, programDayId)

    suspend fun publicProgramDays(programId: String) = src.programDays(programId)

    suspend fun programDay(id: String) = dao.programDay(id)

    suspend fun nextProgramDay() = dao.nextProgramDay()

    suspend fun insert(programDay: ProgramDay) {
        dao.insert(programDay)
        src.scheduleUpload(programDay.id, UploadProgramDayWorker::class.java)
    }

    suspend fun update(programDays: List<ProgramDay>) {
        if (programDays.isEmpty()) return

        dao.update(programDays)
        src.scheduleUpload(programDays[0].programId, UploadProgramDayListWorker::class.java)
    }

    suspend fun delete(id: String) {
        dao.delete(id)
        src.scheduleDeletion(id, UploadProgramDayWorker::class.java)
    }

    fun uploadProgramDay(programDay: ProgramDay) {
        src.uploadProgramDay(programDay)
    }

    fun uploadProgramDays(programDays: List<ProgramDay>) {
        programDays.forEach { src.uploadProgramDay(it) }
    }

    suspend fun syncProgramDays(lastUpdate: Long) {
        val items = src.newProgramDays(lastUpdate)
        val (deletedItems, existingItems) = items.partition { it.deleted }

        deletedItems.forEach { dao.delete(it.id) }
        dao.insert(existingItems)
    }
}