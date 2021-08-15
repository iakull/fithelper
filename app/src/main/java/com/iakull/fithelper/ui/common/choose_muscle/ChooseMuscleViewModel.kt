package com.iakull.fithelper.ui.common.choose_muscle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.repository.MuscleInExerciseRepository
import com.iakull.fithelper.data.repository.ProgramRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.launch

class ChooseMuscleFragmentViewModel(private val repository: MuscleInExerciseRepository) : ViewModel() {

    /*val programs = repository.programsFlow().asLiveData()
    val programPublishedEvent = MutableLiveData<Event<Unit>>()

    fun publishProgram(program: Program) = viewModelScope.launch {
        repository.publishProgram(program)
        programPublishedEvent.value = Event(Unit)
    }*/
}