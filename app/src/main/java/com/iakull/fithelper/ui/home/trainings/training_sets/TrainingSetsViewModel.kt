package com.iakull.fithelper.ui.home.trainings.training_sets

import androidx.lifecycle.*
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.relation.SetWithPreviousResults
import com.iakull.fithelper.data.repository.ExerciseRepository
import com.iakull.fithelper.data.repository.TrainingExerciseRepository
import com.iakull.fithelper.data.repository.TrainingSetRepository
import com.iakull.fithelper.util.live_data.AbsentLiveData
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class TrainingSetsViewModel(
        private val trainingSetRepository: TrainingSetRepository,
        private val trainingExerciseRepo: TrainingExerciseRepository,
        private val exerciseRepo: ExerciseRepository
) : ViewModel() {

    val trainingExerciseId = MutableLiveData<String>()

    val trainingExercise = trainingExerciseId.switchMap {
        trainingExerciseRepo.trainingExerciseFlow(it).asLiveData()
    }

    val exercise = trainingExercise.switchMap {
        liveData {
            emit(exerciseRepo.exercise(it.exercise))
        }
    }

    val currentSets = trainingExercise.switchMap {
        trainingSetRepository.trainingSetsFlow(it.id).asLiveData()
    }

    private val previousExercise = trainingExercise.switchMap {
        liveData { emit(trainingExerciseRepo.previousTrainingExercise(it.trainingId, it.exercise)) }
    }

    val previousSets = previousExercise.switchMap {
        if (it == null) AbsentLiveData.create()
        else trainingSetRepository.trainingSetsFlow(it.trainingExerciseId).asLiveData()
    }

    val sets = MediatorLiveData<List<SetWithPreviousResults>>()

    val trainingExerciseFinishedEvent = MutableLiveData<Event<Unit>>()
    val trainingExerciseDeletedEvent = MutableLiveData<Event<Unit>>()

    init {
        sets.addSource(currentSets) { sets.value = updateSets() }
        sets.addSource(previousSets) { sets.value = updateSets() }
    }

    private fun updateSets(): List<SetWithPreviousResults> {
        val currSets = currentSets.value
        val prevSets = previousSets.value
        val res = mutableListOf<SetWithPreviousResults>()
        for (i in 0 until max(currSets?.size ?: 0, prevSets?.size ?: 0)) {
            val curr = try {
                currSets?.get(i)
            } catch (e: Exception) {
                null
            }
            val prev = try {
                prevSets?.get(i)
            } catch (e: Exception) {
                null
            }
            val set = SetWithPreviousResults(
                    curr?.id ?: "", curr?.weight, curr?.reps, curr?.time, curr?.distance,
                    prev?.id ?: "", prev?.weight, prev?.reps, prev?.time, prev?.distance
            )
            res.add(set)
        }
        return res
    }

    fun deleteSet(id: String) = viewModelScope.launch {
        trainingSetRepository.delete(id)
    }

    fun finishExercise() = viewModelScope.launch {
        trainingExerciseRepo.updateState(trainingExerciseId.value!!, TrainingExercise.FINISHED)
        trainingExercise.value?.let { exerciseRepo.increaseExecutionsCnt(it.exercise) }
        trainingExerciseFinishedEvent.value = Event(Unit)
    }

    fun deleteExercise() {
        val id = trainingExerciseId.value ?: return
        CoroutineScope(Dispatchers.IO).launch { trainingExerciseRepo.delete(id) }
        trainingExerciseDeletedEvent.value = Event(Unit)
    }
}