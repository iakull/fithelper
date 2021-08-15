package com.iakull.fithelper.ui.home.programs.create_program_day

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.repository.ProgramDayRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.launch

class CreateProgramDayViewModel(private val programDayRepo: ProgramDayRepository) : ViewModel() {

    val name = MutableLiveData("")
    val description = MutableLiveData("")

    val programDayCreatedEvent = MutableLiveData<Event<String>>()

    fun createProgramDay(programId: String, num: Int) = viewModelScope.launch {
        val programDay = ProgramDay(programId, num, name.value!!, description.value!!)
        programDayRepo.insert(programDay)

        programDayCreatedEvent.value = Event(programDay.id)
    }
}