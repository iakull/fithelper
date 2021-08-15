package com.iakull.fithelper.ui.home.trainings.exercises

import androidx.lifecycle.*
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.repository.ExerciseRepository
import com.iakull.fithelper.data.repository.TrainingExerciseRepository
import com.iakull.fithelper.data.repository.TrainingRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingExercisesViewModel(
        private val trainingRepo: TrainingRepository,
        private val trainingExerciseRepo: TrainingExerciseRepository,
        private val exerciseRepo: ExerciseRepository
) : ViewModel() {

    val trainingId = MutableLiveData<String>()
    val training = trainingId.switchMap { liveData { emit(trainingRepo.training(it)) } }

    private val exercises = trainingId.switchMap {
        trainingExerciseRepo.trainingExercisesFlow(it).asLiveData()
    }

    val runningExercises = exercises.map {
        it.filter { exercise -> exercise.state == TrainingExercise.RUNNING }
    }

    val plannedExercises = exercises.map {
        it.filter { exercise -> exercise.state == TrainingExercise.PLANNED }
    }

    val finishedExercises = exercises.map {
        it.filter { exercise -> exercise.state == TrainingExercise.FINISHED }
    }

    val trainingFinishedEvent = MutableLiveData<Event<Unit>>()
    val trainingDeletedEvent = MutableLiveData<Event<Unit>>()

    fun deleteExercise(exercise: TrainingExercise) = viewModelScope.launch {
        trainingExerciseRepo.delete(exercise.id)
        exerciseRepo.decreaseExecutionsCnt(exercise.exercise)
    }

    fun finishTraining(duration: Int) {
        viewModelScope.launch {
            val training = training.value ?: return@launch
            trainingRepo.update(training.copy(duration = duration))
            trainingExerciseRepo.update(
                    exercises.value!!.map { it.copy(state = TrainingExercise.FINISHED) }
            )
            trainingFinishedEvent.value = Event(Unit)
        }
    }

    fun finishExercise(exercise: TrainingExercise) = viewModelScope.launch {
        trainingExerciseRepo.updateState(exercise.id, TrainingExercise.FINISHED)
        exerciseRepo.increaseExecutionsCnt(exercise.exercise)
    }

    fun updateIndexNumbers() = CoroutineScope(Dispatchers.IO).launch {
        val exercises = plannedExercises.value ?: return@launch
        val newList = exercises.mapIndexed { index, exercise ->
            exercise.copy(indexNumber = index + 1)
        }
        trainingExerciseRepo.update(newList)
    }

    fun deleteTraining() {
        val id = trainingId.value ?: return
        CoroutineScope(Dispatchers.IO).launch { trainingRepo.delete(id) }
        trainingDeletedEvent.value = Event(Unit)
    }
}