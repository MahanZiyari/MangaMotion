package ir.mahan.mangamotion.data.repository

import ir.mahan.mangamotion.data.model.anime.AnimeEntity
import ir.mahan.mangamotion.data.source.LocalDataSource
import ir.mahan.mangamotion.data.source.RemoteDataSource
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {
    ///////////////////////////////////////////////////////////////////////////
    // Remote
    ///////////////////////////////////////////////////////////////////////////
    suspend fun fetchTopAnimeList(query: Map<String, String>) = remoteDataSource.getTopAnime(query)
    suspend fun searchAnime(query: Map<String, String>) = remoteDataSource.searchAnime(query)

    ///////////////////////////////////////////////////////////////////////////
    // Local
    ///////////////////////////////////////////////////////////////////////////
    suspend fun getAnimeResponseByDB(id: Int) = localDataSource.getAnimeResponse(id)
    suspend fun checkAnimeResponseExist(id: Int) = localDataSource.checkAnimeResponseExist(id)
    suspend fun saveAnimeResponse(animeEntity: AnimeEntity) =  localDataSource.saveAnimeResponse(animeEntity)
}