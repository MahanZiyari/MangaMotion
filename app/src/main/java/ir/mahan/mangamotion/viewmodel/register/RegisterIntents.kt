package ir.mahan.mangamotion.viewmodel.register

import ir.mahan.mangamotion.data.model.AuthInfo

sealed class RegisterIntents {
    data class SignInUser(val authInfo: AuthInfo) : RegisterIntents()
    data class SignUpUser(val authInfo: AuthInfo) : RegisterIntents()
}