package com.iakull.fithelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.repository.ProgramRepository
import com.iakull.fithelper.data.repository.TrainingRepository
import kotlinx.coroutines.launch

class HomeViewModel(
        private val trainingRepo: TrainingRepository,
        private val programRepo: ProgramRepository
) : ViewModel() {

    val trainings = trainingRepo.detailedTrainingsFlow().asLiveData()

    val programs = programRepo.programsFlow().asLiveData()

    fun deleteProgram(id: String) = viewModelScope.launch {
        programRepo.delete(id)
    }
}