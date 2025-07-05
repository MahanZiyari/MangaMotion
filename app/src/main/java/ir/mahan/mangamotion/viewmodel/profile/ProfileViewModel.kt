package ir.mahan.mangamotion.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.data.model.profile.ProfileInfo
import ir.mahan.mangamotion.data.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val intents: Channel<ProfileIntents> = Channel()
    private val _states = MutableStateFlow<ProfileStates>(ProfileStates.Idle)
    val states get() = _states


    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                is ProfileIntents.LoadUserProfile -> fetchUserprofile()
            }
        }
    }

    private fun fetchUserprofile() = viewModelScope.launch {
        _states.value = ProfileStates.LoadProfileData(
            ProfileInfo(
                uuid = sessionManager.currentUserId,
                email = sessionManager.currentUser.first()?.email
            )
        )
    }

}