package ir.mahan.mangamotion.viewmodel.profile

import ir.mahan.mangamotion.data.model.profile.ProfileInfo

sealed class ProfileStates {
    data object Idle : ProfileStates()
    data class LoadProfileData(val profileInfo: ProfileInfo): ProfileStates()
}