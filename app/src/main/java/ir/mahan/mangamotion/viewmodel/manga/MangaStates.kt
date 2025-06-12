package ir.mahan.mangamotion.viewmodel.manga

import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.utils.Wrapper

sealed class MangaStates {
    data object Idle : MangaStates()
    data object TopMangasLoading: MangaStates()
    data class ShowTopMangas(val topMangas: List<ResponseTopManga.Data>) : MangaStates()
    data object NewMangasLoading: MangaStates()
    data class ShowNewMangas(val newMangas: List<ResponseTopManga.Data>) : MangaStates()
    data class ShowDoujins(val doujins: List<ResponseTopManga.Data>) : MangaStates()
    data class ShowManhwas(val manhwas: List<ResponseTopManga.Data>) : MangaStates()
    data class ShowManhuas(val manhuas: List<ResponseTopManga.Data>) : MangaStates()
    data class Error(val message: String) : MangaStates()
}