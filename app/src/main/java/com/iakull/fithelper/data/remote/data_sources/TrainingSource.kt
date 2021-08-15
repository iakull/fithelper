package com.iakull.fithelper.data.remote.data_sources

import androidx.work.WorkManager
import com.iakull.fithelper.data.model.Training
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.model.TrainingSet
import com.iakull.fithelper.data.remote.firestore
import com.iakull.fithelper.data.remote.uid

class TrainingSource(workManager: WorkManager) : RemoteDataSource(workManager) {

    private val trainingsRef
        get() = firestore.collection("users/$uid/trainings")
    private val trainingExercisesRef
        get() = firestore.collection("users/$uid/training_exercises")
    private val trainingSetsRef
        get() = firestore.collection("users/$uid/training_sets")

    suspend fun newTrainings(lastUpdate: Long) =
            getNewData(Training::class.java, trainingsRef, lastUpdate)

    suspend fun newTrainingExercises(lastUpdate: Long) =
            getNewData(TrainingExercise::class.java, trainingExercisesRef, lastUpdate)

    suspend fun newTrainingSets(lastUpdate: Long) =
            getNewData(TrainingSet::class.java, trainingSetsRef, lastUpdate)

    fun uploadTraining(training: Training) {
        trainingsRef.document(training.id).set(training)
    }

    fun uploadTrainingExercise(trainingExercise: TrainingExercise) {
        trainingExercisesRef.document(trainingExercise.id).set(trainingExercise)
    }

    fun uploadTrainingSet(trainingSet: TrainingSet) {
        trainingSetsRef.document(trainingSet.id).set(trainingSet)
    }
}