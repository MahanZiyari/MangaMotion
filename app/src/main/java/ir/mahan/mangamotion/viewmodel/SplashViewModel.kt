package ir.mahan.mangamotion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.ui.splash.SplashIntents
import ir.mahan.mangamotion.ui.splash.SplashStates
import ir.mahan.mangamotion.data.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val sessionManager: SessionManager): ViewModel() {

    val intents: Channel<SplashIntents> = Channel()
    private val _states = MutableStateFlow<SplashStates>(SplashStates.Idle)
    val states get() = _states

    init {
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
        _states.value = if (sessionManager.hasUser()) {
            SplashStates.NavigateToHome
        } else {
            SplashStates.NavigateToLogin
        }
    }
    
}