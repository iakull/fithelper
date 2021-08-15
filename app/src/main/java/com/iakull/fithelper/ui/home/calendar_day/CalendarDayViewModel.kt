package com.iakull.fithelper.ui.home.calendar_day

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.iakull.fithelper.data.repository.TrainingRepository
import java.util.*

class CalendarDayViewModel(
        private val trainingRepo: TrainingRepository
) : ViewModel() {

    val date = MutableLiveData<Date>()

    val trainings = date.switchMap {
        liveData {
            emit(trainingRepo.detailedTrainingsForDay(it))
        }
    }
}