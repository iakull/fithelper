package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.TrainingExerciseDao
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.remote.data_sources.TrainingSource
import com.iakull.fithelper.workers.UploadTrainingExerciseListWorker
import com.iakull.fithelper.workers.UploadTrainingExerciseWorker

class TrainingExerciseRepository(
        private val dao: TrainingExerciseDao,
        private val src: TrainingSource
) {

    fun trainingExercisesFlow(trainingId: String) = dao.trainingExercisesFlow(trainingId)

    suspend fun trainingExercises(trainingId: String) = dao.trainingExercises(trainingId)

    suspend fun previousTrainingExercise(trainingId: String, exercise: String) = dao.previousTrainingExercise(trainingId, exercise)

    fun trainingExerciseFlow(id: String) = dao.trainingExerciseFlow(id)

    suspend fun trainingExercise(id: String) = dao.trainingExercise(id)

    suspend fun insert(exercise: TrainingExercise) {
        dao.insert(exercise)
        src.scheduleUpload(exercise.id, UploadTrainingExerciseWorker::class.java)
    }

    suspend fun insert(trainingExercises: List<TrainingExercise>) {
        if (trainingExercises.isEmpty()) return

        dao.insert(trainingExercises)
        src.scheduleUpload(trainingExercises[0].trainingId, UploadTrainingExerciseListWorker::class.java)
    }

    suspend fun updateState(id: String, state: Int) {
        val exercise = dao.trainingExercise(id)
        val updatedExercise = exercise.copy(state = state)
        dao.update(updatedExercise)
        src.scheduleUpload(exercise.id, UploadTrainingExerciseWorker::class.java)
    }

    suspend fun update(trainingExercises: List<TrainingExercise>) {
        if (trainingExercises.isEmpty()) return

        dao.update(trainingExercises)
        src.scheduleUpload(trainingExercises[0].trainingId, UploadTrainingExerciseListWorker::class.java)
    }

    suspend fun delete(id: String) {
        dao.delete(id)
        src.scheduleDeletion(id, UploadTrainingExerciseWorker::class.java)
    }

    fun uploadTrainingExercises(trainingExercises: List<TrainingExercise>) {
        trainingExercises.forEach { src.uploadTrainingExercise(it) }
    }

    fun uploadTrainingExercise(trainingExercise: TrainingExercise) {
        src.uploadTrainingExercise(trainingExercise)
    }

    suspend fun syncTrainingExercises(lastUpdate: Long) {
        val items = src.newTrainingExercises(lastUpdate)
        val (deletedItems, existingItems) = items.partition { it.deleted }

        deletedItems.forEach { dao.delete(it.id) }
        dao.insert(existingItems)
    }
}