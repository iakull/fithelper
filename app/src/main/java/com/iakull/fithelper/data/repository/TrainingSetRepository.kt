package com.iakull.fithelper.data.repository

import com.iakull.fithelper.data.local.dao.TrainingSetDao
import com.iakull.fithelper.data.model.TrainingSet
import com.iakull.fithelper.data.remote.data_sources.TrainingSource
import com.iakull.fithelper.workers.UploadTrainingSetWorker

class TrainingSetRepository(private val dao: TrainingSetDao, private val src: TrainingSource) {

    fun trainingSetsFlow(trainingExerciseId: String) = dao.trainingSetsFlow(trainingExerciseId)

    suspend fun trainingSet(id: String) = dao.trainingSet(id)

    suspend fun insert(set: TrainingSet) {
        dao.insert(set)
        src.scheduleUpload(set.id, UploadTrainingSetWorker::class.java)
    }

    suspend fun update(set: TrainingSet) {
        dao.update(set)
        src.scheduleUpload(set.id, UploadTrainingSetWorker::class.java)
    }

    suspend fun delete(id: String) {
        dao.delete(id)
        src.scheduleDeletion(id, UploadTrainingSetWorker::class.java)
    }

    fun uploadTrainingSet(trainingSet: TrainingSet) {
        src.uploadTrainingSet(trainingSet)
    }

    suspend fun syncTrainingSets(lastUpdate: Long) {
        val items = src.newTrainingSets(lastUpdate)
        val (deletedItems, existingItems) = items.partition { it.deleted }

        deletedItems.forEach { dao.delete(it.id) }
        dao.insert(existingItems)
    }
}