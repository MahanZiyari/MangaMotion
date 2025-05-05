package ir.mahan.mangamotion.viewmodel.splash

sealed class SplashStates {
    data object Idle: SplashStates()
    data object NavigateToLogin: SplashStates()
    data object NavigateToHome: SplashStates()
}