package com.iakull.fithelper.ui.home.trainings.create_training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.model.Training
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.repository.*
import com.iakull.fithelper.util.live_data.Event
import com.iakull.fithelper.util.setNewValue
import com.iakull.fithelper.util.toDate
import com.iakull.fithelper.util.toIsoString
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class CreateTrainingViewModel(
        private val trainingRepo: TrainingRepository,
        private val trainingExerciseRepo: TrainingExerciseRepository,
        private val programRepo: ProgramRepository,
        private val programDayRepo: ProgramDayRepository,
        private val programDayExerciseRepo: ProgramDayExerciseRepository
) : ViewModel() {

    val dateTime = MutableLiveData(LocalDateTime.now())

    val program = MutableLiveData<Program>()
    val programDay = MutableLiveData<ProgramDay>()

    val byProgram = MutableLiveData(false)

    val trainingCreatedEvent = MutableLiveData<Event<String>>()

    init {
        viewModelScope.launch {
            programDayRepo.nextProgramDay()?.let { day ->
                program.setNewValue(programRepo.program(day.programId))
                programDay.setNewValue(day)
                byProgram.setNewValue(true)
            }
        }
    }

    fun createTraining() = viewModelScope.launch {
        val programDayId = byProgram.value?.let { programDay.value?.id }
        val startDateTime = dateTime.value!!.toIsoString().toDate()!!
        val training = Training(startDateTime, programDayId)
        trainingRepo.insert(training)
        if (programDayId != null) fillTrainingWithProgramExercises(training.id, programDayId)

        trainingCreatedEvent.value = Event(training.id)
    }

    private suspend fun fillTrainingWithProgramExercises(trainingId: String, programDayId: String) {
        val programDayExercises = programDayExerciseRepo.programDayExercises(programDayId)
        val trainingExercises = programDayExercises.map {
            TrainingExercise(trainingId, it.exercise, it.indexNumber, it.rest)
        }
        trainingExerciseRepo.insert(trainingExercises)
    }
}