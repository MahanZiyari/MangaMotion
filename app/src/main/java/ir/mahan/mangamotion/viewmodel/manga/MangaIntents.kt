package ir.mahan.mangamotion.viewmodel.manga

sealed class MangaIntents {
    data object LoadTopMangas: MangaIntents()
}