package com.iakull.fithelper.ui.home.programs.create_program

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.repository.ProgramRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.launch

class CreateProgramViewModel(private val repository: ProgramRepository) : ViewModel() {

    val name = MutableLiveData("")
    val description = MutableLiveData("")
    val author = MutableLiveData<User?>()

    val programCreatedEvent = MutableLiveData<Event<String>>()

    fun createProgram() = viewModelScope.launch {
        val author = author.value
        val program = Program(name.value!!, description.value!!, author?.id, author?.name)
        repository.insert(program)
        programCreatedEvent.value = Event(program.id)
    }
}