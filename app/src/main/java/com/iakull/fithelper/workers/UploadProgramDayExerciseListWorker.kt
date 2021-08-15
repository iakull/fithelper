package com.iakull.fithelper.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iakull.fithelper.data.remote.data_sources.ID
import com.iakull.fithelper.data.repository.ProgramDayExerciseRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class UploadProgramDayExerciseListWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repo: ProgramDayExerciseRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            val programDayId = inputData.getString(ID)!!
            repo.uploadProgramDayExercises(repo.programDayExercises(programDayId))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}