package com.iakull.fithelper.ui.common.exercises

import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.iakull.fithelper.data.local.dao.MuscleDao
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.ProgramDayExercise
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.relation.FilterParam
import com.iakull.fithelper.data.repository.ExerciseRepository
import com.iakull.fithelper.data.repository.ProgramDayExerciseRepository
import com.iakull.fithelper.data.repository.TrainingExerciseRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class ExercisesViewModel(
        private val exerciseTargetDao: MuscleDao,
        private val exerciseRepo: ExerciseRepository,
        private val trainingExerciseRepository: TrainingExerciseRepository,
        private val programDayExerciseRepository: ProgramDayExerciseRepository
) : ViewModel() {

    private val query = MediatorLiveData<SupportSQLiteQuery>()
    val searchText = MutableLiveData<String>()
    val exerciseList = query.switchMap { query -> exerciseRepo.exercisesFlow(query).asLiveData() }
    val addedToFavorite = MutableLiveData<Boolean>()
    val exerciseTargetList = liveData { emit(exerciseTargetDao.params()) }
    val exerciseAddedEvent = MutableLiveData<Event<Unit>>()

    init {
        listOf(searchText, addedToFavorite, exerciseTargetList)
                .forEach { query.addSource(it) { updateQuery() } }
    }

    fun updateQuery() {
        val sb = StringBuilder("SELECT * FROM exercise WHERE name IS NOT NULL")
        searchText.value?.let { if (it.trim().isNotEmpty()) sb.append(" AND name LIKE '%$it%'") }
        if (addedToFavorite.value == true) sb.append(" AND isFavorite == 1")
        val res = sb.toString()
        Timber.d("QUERY = $res")
        query.value = SimpleSQLiteQuery(res)
    }

    fun setFavorite(exercise: Exercise) = viewModelScope.launch {
        exerciseRepo.setFavorite(exercise.name, !exercise.isFavorite)
    }

    fun setChecked(filterParams: LiveData<List<FilterParam>>, name: String, isChecked: Boolean) {
        filterParams.value?.find { it.name == name }?.isActive = isChecked
        updateQuery()
    }

    fun addExerciseToTraining(exercise: Exercise, trainingId: String, num: Int) = viewModelScope.launch {
        val trainingExercise = TrainingExercise(trainingId, exercise.name, num)
        trainingExerciseRepository.insert(trainingExercise)
        exerciseAddedEvent.value = Event(Unit)
    }

    fun addExerciseToProgramDay(exercise: Exercise, programDayId: String, num: Int) = viewModelScope.launch {
        programDayExerciseRepository.insert(
                ProgramDayExercise(programDayId, exercise.name, num)
        )
        exerciseAddedEvent.value = Event(Unit)
    }

    private fun LiveData<List<FilterParam>>.getActiveParamsString() =
            value?.filter { it.isActive }?.joinToString(", ") { "'${it.name}'" } ?: ""
}
