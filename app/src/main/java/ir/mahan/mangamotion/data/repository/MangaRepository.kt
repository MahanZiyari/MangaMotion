package ir.mahan.mangamotion.data.repository

import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.source.LocalDataSource
import ir.mahan.mangamotion.data.source.RemoteDataSource
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {
    ///////////////////////////////////////////////////////////////////////////
    // Remote
    ///////////////////////////////////////////////////////////////////////////
    suspend fun fetchTopMangas(query: Map<String, String>) = remoteDataSource.getTopMangas(query)
    suspend fun searchManga(query: Map<String, String>) = remoteDataSource.searchManga(query)

    ///////////////////////////////////////////////////////////////////////////
    // Local
    ///////////////////////////////////////////////////////////////////////////
    suspend fun getMangasByDB(id: Int) = localDataSource.getMangaResponse(id)
    suspend fun checkMangaResponseExist(id: Int) = localDataSource.checkMangaResponseExist(id)
    suspend fun saveMangaResponse(mangaEntity: MangaEntity) =  localDataSource.saveMangaResponse(mangaEntity)
}