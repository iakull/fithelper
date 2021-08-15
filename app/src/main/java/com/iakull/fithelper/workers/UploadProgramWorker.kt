package com.iakull.fithelper.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iakull.fithelper.data.remote.data_sources.ID
import com.iakull.fithelper.data.remote.data_sources.NEED_TO_DELETE
import com.iakull.fithelper.data.repository.ProgramRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class UploadProgramWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repo: ProgramRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            val id = inputData.getString(ID)!!
            val needToDelete = inputData.getBoolean(NEED_TO_DELETE, false)

            repo.uploadProgram(repo.program(id).copy(deleted = needToDelete, lastUpdate = Date()))

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}