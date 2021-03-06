package com.iakull.fithelper.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.iid.FirebaseInstanceId
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.remote.firebaseUser
import com.iakull.fithelper.data.remote.firestore
import com.iakull.fithelper.data.remote.userDocument
import com.iakull.fithelper.data.remote.userTokensDocument
import com.iakull.fithelper.util.PreferencesKeys
import com.iakull.fithelper.util.live_data.AbsentLiveData
import com.iakull.fithelper.util.live_data.Event
import com.iakull.fithelper.util.live_data.liveData
import com.iakull.fithelper.workers.SyncLocalDatabaseWorker
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.TimeUnit

private const val LOCAL_DB_SYNC = "local db sync"

class SharedViewModel(
        private val workManager: WorkManager,
        private val preferences: SharedPreferences
) : ViewModel() {

    val program = MutableLiveData<Event<Program>>()
    val programDay = MutableLiveData<Event<ProgramDay>>()

    //region Firebase
    private val userExist = MutableLiveData<Boolean>()

    val user = userExist.switchMap { userExist ->
        if (!userExist) AbsentLiveData.create()
        else userDocument.liveData<User?> { it.toObject(User::class.java)!! }
    }

    fun initUser() {
        firebaseUser?.let { userExist.value = true }
    }

    fun signOut() {
        userExist.value = false
        workManager.cancelAllWorkByTag(LOCAL_DB_SYNC)
    }

    fun signIn() {
        viewModelScope.launch {
            val tokensTask = userTokensDocument.get()
            val result = FirebaseInstanceId.getInstance().instanceId.await()
            val tokensDoc = tokensTask.await()
            if (!tokensDoc.exists()) createNewUser(result.token)
            else addToken(result.token, tokensDoc)
            userExist.value = true
            runLocalDatabasePeriodicSync()
        }
    }

    private fun runLocalDatabasePeriodicSync() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

        val request = PeriodicWorkRequest
                .Builder(SyncLocalDatabaseWorker::class.java, 1, TimeUnit.DAYS)
                .addTag(LOCAL_DB_SYNC)
                .setConstraints(constraints)
                .build()
        workManager.enqueue(request)
    }

    private suspend fun createNewUser(token: String) {
        val newUser = User(
                firebaseUser!!.uid,
                firebaseUser!!.displayName ?: "",
                firebaseUser!!.photoUrl.toString()
        )
        firestore.batch()
                .set(userDocument, newUser)
                .set(userTokensDocument, mapOf("tokens" to listOf(token)))
                .commit()
                .await()
    }

    private fun addToken(token: String, tokensDoc: DocumentSnapshot) {
        @Suppress("UNCHECKED_CAST")
        val tokens = tokensDoc["tokens"] as MutableList<String>
        if (!tokens.contains(token)) {
            tokens.add(token)
            userTokensDocument.update("tokens", tokens)
        }
    }

    //region Timer
    private var timer: Timer? = null
    val timerIsRunning = MutableLiveData(false)
    var sessionStartMillis = 0L
    var restStartMillis = 0L
    val elapsedSessionTime = MutableLiveData<Int?>()
    val elapsedRestTime = MutableLiveData<Int?>()
    val restTime = MutableLiveData<Int?>()

    fun startTimer() {
        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                val now = System.currentTimeMillis()
                elapsedSessionTime.postValue(((now - sessionStartMillis) / 1000).toInt())
                if (restStartMillis != 0L) {
                    elapsedRestTime.postValue(((now - restStartMillis) / 1000).toInt())
                }
            }
        }
        timer?.scheduleAtFixedRate(task, 0, 1000)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    fun onTrainingSessionStarted() {
        sessionStartMillis = System.currentTimeMillis()
        timerIsRunning.value = true
        clearValues()
        startTimer()
        preferences.edit {
            putBoolean(PreferencesKeys.TIMER_IS_RUNNING, true)
            putLong(PreferencesKeys.SESSION_START_MILLIS, sessionStartMillis)
        }
    }

    fun onTrainingSessionFinished() {
        stopTimer()
        timerIsRunning.value = false
        clearValues()
        preferences.edit {
            putBoolean(PreferencesKeys.TIMER_IS_RUNNING, false)
            remove(PreferencesKeys.SESSION_START_MILLIS)
            remove(PreferencesKeys.REST_START_MILLIS)
            remove(PreferencesKeys.REST_TIME)
        }
    }

    fun onSetCompleted(rest: Int) {
        restStartMillis = System.currentTimeMillis()
        restTime.value = rest
        preferences.edit {
            putLong(PreferencesKeys.REST_START_MILLIS, restStartMillis)
            putInt(PreferencesKeys.REST_TIME, rest)
        }
    }

    private fun clearValues() {
        elapsedSessionTime.value = null
        elapsedRestTime.value = null
        restTime.value = null
    }
}