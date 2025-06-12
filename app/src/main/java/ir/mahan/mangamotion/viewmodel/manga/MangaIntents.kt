package ir.mahan.mangamotion.viewmodel.manga

sealed class MangaIntents {
    data object LoadTopMangas: MangaIntents()
    data object LoadNewMangas: MangaIntents()
    data object LoadDoujins: MangaIntents()
    data object LoadManhwas: MangaIntents()
    data object LoadManhuas: MangaIntents()
}