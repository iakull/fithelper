package com.iakull.fithelper.ui.profile.edit_profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.remote.data_sources.ImageSource
import com.iakull.fithelper.data.remote.userDocument
import com.iakull.fithelper.util.setNewValue
import kotlinx.coroutines.launch

class EditProfileViewModel(private val image: ImageSource) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val name = MutableLiveData<String>()
    val photoUrl = MutableLiveData<String>()
    val gender = MutableLiveData("")
    val trainingTarget = MutableLiveData("")
    val height = MutableLiveData("")
    val weight = MutableLiveData("")

    fun setUser(user: User) {
        _user.setNewValue(user)
        name.setNewValue(user.name)

        gender.setNewValue(user.gender)
        trainingTarget.setNewValue(user.trainingTarget)

        height.setNewValue(user.height)
        weight.setNewValue(user.weight)
    }

    fun saveChanges() {
        val user = _user.value!!.copy(
                name = name.value!!,

                gender = gender.value!!,
                trainingTarget = trainingTarget.value!!,
                height = height.value!!,
                weight = weight.value!!
        )
        userDocument.set(user)
    }

    fun saveImage(uri: Uri) {
        viewModelScope.launch {
            photoUrl.setNewValue(null)
            val imageUri = image.uploadImage(uri)
            photoUrl.setNewValue(imageUri)
        }
    }
}