package com.iakull.fithelper.ui.common.create_post

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iakull.fithelper.data.model.Post
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.remote.data_sources.ImageSource
import com.iakull.fithelper.data.remote.data_sources.PostSource
import com.iakull.fithelper.data.repository.ProgramRepository
import com.iakull.fithelper.util.live_data.Event
import kotlinx.coroutines.launch

class CreatePostViewModel(
        private val postSrc: PostSource,
        private val programRepo: ProgramRepository,
        private val image: ImageSource
) : ViewModel() {

    val user = MutableLiveData<User>()
    val title = MutableLiveData("")
    val content = MutableLiveData("")
    val imageUri = MutableLiveData<Uri?>()
    val program = MutableLiveData<Program>()

    val postPublishedEvent = MutableLiveData<Event<Unit>>()

    fun publishPost() {
        viewModelScope.launch {
            val author = user.value ?: return@launch

            val imageUri = imageUri.value?.let { image.uploadImage(it) }
            program.value?.let { programRepo.publishProgram(it) }

            val post = Post(author.id, author.name, title.value, content.value, imageUri, program.value)
            postSrc.publishPost(post)

            postPublishedEvent.value = Event(Unit)
        }
    }
}