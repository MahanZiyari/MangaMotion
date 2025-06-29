package ir.mahan.mangamotion.viewmodel.anime

sealed class AnimeIntents {
    data object LoadTopAnimeList: AnimeIntents()
    data object LoadNewAnimeList: AnimeIntents()
    data object LoadMovieAnimeList: AnimeIntents()
    data object LoadKidsAnimeList: AnimeIntents()
}