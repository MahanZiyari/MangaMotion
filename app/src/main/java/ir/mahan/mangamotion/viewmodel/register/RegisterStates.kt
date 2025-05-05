package ir.mahan.mangamotion.viewmodel.register

import com.google.firebase.auth.FirebaseUser

sealed class RegisterStates {
    data object Idle : RegisterStates()
    data object Loading : RegisterStates()
    data class SuccessfulLogin(val user: FirebaseUser) : RegisterStates()
    data class FailedLogin(val message: String) : RegisterStates()
}