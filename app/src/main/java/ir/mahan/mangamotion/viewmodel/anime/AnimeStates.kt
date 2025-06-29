package ir.mahan.mangamotion.viewmodel.anime

import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList

sealed class AnimeStates {
    data object LoadingForTopAnimeList: AnimeStates()
    data class ShowTopAnimeList(val animeList: List<ResponseAnimeList.Data>): AnimeStates()
    data object LoadingForNewAnimeList: AnimeStates()
    data class ShowNewAnimeList(val animeList: List<ResponseAnimeList.Data>): AnimeStates()
    data class Error(val message: String): AnimeStates()

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}