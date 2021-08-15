package com.iakull.fithelper.ui.common.create_exercise

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.local.dao.MuscleDao
import com.iakull.fithelper.data.local.dao.MuscleInExerciseDao
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.TargetedMuscle
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.remote.data_sources.ImageSource
import com.iakull.fithelper.data.repository.ExerciseRepository
import kotlinx.coroutines.launch

class CreateExerciseViewModel(
        private val muscleTargetDao: MuscleInExerciseDao,
        private val repositoryExr: ExerciseRepository,
        private val firebaseStorage: ImageSource
) : ViewModel() {

    val user = MutableLiveData<User>()
    val name = MutableLiveData("")
    val description = MutableLiveData("")
    val imageUri = MutableLiveData<Uri?>()

    fun createExr() = viewModelScope.launch {
        val imageUri = imageUri.value?.let { firebaseStorage.uploadImage(it) }
        val exr = Exercise(name.value!!, description.value!!, imageUri)
        repositoryExr.insertExercise(exr)
    }

    fun createMIE() = viewModelScope.launch {
        val exr = TargetedMuscle(name.value!!, description.value!!)
        muscleTargetDao.insertItem(exr)
    }
}