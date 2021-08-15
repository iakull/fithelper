package com.iakull.fithelper.di.feed.program_day_exercises

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.model.ProgramDayExercise
import com.iakull.fithelper.data.repository.ProgramDayExerciseRepository
import com.iakull.fithelper.data.repository.ProgramDayRepository
import com.iakull.fithelper.util.setNewValue
import kotlinx.coroutines.launch

class PublicProgramDayExercisesViewModel(
        private val programDayRepo: ProgramDayRepository,
        private val programDayExerciseRepo: ProgramDayExerciseRepository
) : ViewModel() {

    val programDay = MutableLiveData<ProgramDay>()

    val exercises = MutableLiveData<List<ProgramDayExercise>>()

    fun start(programId: String, programDayId: String) {
        viewModelScope.launch {
            programDay.setNewValue(programDayRepo.publicProgramDay(programId, programDayId))
            exercises.setNewValue(programDayExerciseRepo.publicProgramDayExercises(programId, programDayId))
        }
    }
}