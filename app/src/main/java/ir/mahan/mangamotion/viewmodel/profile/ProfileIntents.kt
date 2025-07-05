package ir.mahan.mangamotion.viewmodel.profile

sealed class ProfileIntents {
    data object LoadUserProfile: ProfileIntents()
}