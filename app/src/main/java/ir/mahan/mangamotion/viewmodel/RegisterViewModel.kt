package ir.mahan.mangamotion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.data.model.AuthInfo
import ir.mahan.mangamotion.ui.register.RegisterIntents
import ir.mahan.mangamotion.ui.register.RegisterStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val sessionManager: SessionManager) :
    ViewModel() {

    val intents: Channel<RegisterIntents> = Channel()
    private val _states = MutableStateFlow<RegisterStates>(RegisterStates.Idle)
    val states get() = _states

    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                is RegisterIntents.SignInUser -> {}
                is RegisterIntents.SignUpUser -> signUpUser(intent.authInfo)
            }

        }
    }

    private fun signUpUser(authInfo: AuthInfo) = viewModelScope.launch(Dispatchers.IO) {
        _states.value = RegisterStates.Loading
        try {
            sessionManager.signUp(authInfo)
            _states.value = RegisterStates.SuccessfulLogin(sessionManager.currentUser.last()!!)
        } catch (e: Exception) {
            _states.value = RegisterStates.FailedLogin(e.message.toString())
        }
    }

}