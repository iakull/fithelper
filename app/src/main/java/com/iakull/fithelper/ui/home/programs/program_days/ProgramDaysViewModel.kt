package com.iakull.fithelper.ui.home.programs.program_days

import androidx.lifecycle.*
import com.iakull.fithelper.data.repository.ProgramDayRepository
import com.iakull.fithelper.data.repository.ProgramRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgramDaysViewModel(
        private val programRepo: ProgramRepository,
        private val programDayRepo: ProgramDayRepository
) : ViewModel() {

    val programId = MutableLiveData<String>()

    val programDays = programId.switchMap { programDayRepo.programDaysFlow(it).asLiveData() }

    val programDeletedEvent = MutableLiveData<Event<Unit>>()

    fun deleteProgramDay(id: String) = viewModelScope.launch {
        programDayRepo.delete(id)
    }

    fun deleteProgram() {
        val id = programId.value ?: return
        CoroutineScope(Dispatchers.IO).launch { programRepo.delete(id) }
        programDeletedEvent.value = Event(Unit)
    }

    fun updateIndexNumbers() {
        CoroutineScope(Dispatchers.IO).launch {
            val programDays = programDays.value ?: return@launch
            val newList = programDays.mapIndexed { index, programDay ->
                programDay.copy(indexNumber = index + 1)
            }
            programDayRepo.update(newList)
        }
    }
}