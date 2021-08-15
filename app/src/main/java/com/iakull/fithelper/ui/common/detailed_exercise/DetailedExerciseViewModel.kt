package com.iakull.fithelper.ui.common.detailed_exercise

import androidx.lifecycle.*
import com.iakull.fithelper.data.local.dao.MuscleInExerciseDao
import com.iakull.fithelper.data.repository.ExerciseRepository
import kotlinx.coroutines.launch

class DetailedExerciseViewModel(
        private val muscleInExerciseDao: MuscleInExerciseDao,
        private val repository: ExerciseRepository
) : ViewModel() {

    val exerciseName = MutableLiveData<String>()
    val exercise = exerciseName.switchMap { liveData { emit(repository.exercise(it)) } }

    val targetedMuscles = exerciseName.switchMap {
        liveData {
            emit(muscleInExerciseDao.targetedMuscles(it).joinToString(", "))
        }
    }

    fun setFavorite(isChecked: Boolean) = viewModelScope.launch {
        exercise.value?.let { repository.setFavorite(it.name, isChecked) }
    }

    fun deleteExercise() = viewModelScope.launch {
        exercise.value?.let { repository.delete(it.name) }
    }
}