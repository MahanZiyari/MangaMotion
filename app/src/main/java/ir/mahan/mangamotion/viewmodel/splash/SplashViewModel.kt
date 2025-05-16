package ir.mahan.mangamotion.viewmodel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val sessionManager: SessionManager): ViewModel() {

    val intents: Channel<SplashIntents> = Channel()
    private val _states = MutableStateFlow<SplashStates>(SplashStates.Idle)
    val states get() = _states

    init {
        //viewModelScope.launch { sessionManager.signOut() }
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect{intent ->
            when(intent) {
                SplashIntents.CheckUser -> checkIfUserSignedIn()
            }

        }
    }

    private fun checkIfUserSignedIn() = viewModelScope.launch {
        /*_states.value = if (sessionManager.hasUser()) {
            Timber.tag(DEBUG_TAG).d("User Found")
            SplashStates.NavigateToHome
        } else {
            Timber.tag(DEBUG_TAG).d("User NOT Found")
            SplashStates.NavigateToLogin
        }*/
        sessionManager.currentUser.collect {
            if (it != null) {
                Timber.tag(DEBUG_TAG).d("User: ${it.email}")
                _states.value = SplashStates.NavigateToHome
            } else {
                _states.value = SplashStates.NavigateToLogin
            }
        }
    }
    
}