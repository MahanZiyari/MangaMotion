package ir.mahan.mangamotion.data.repository

import ir.mahan.mangamotion.data.source.RemoteDataSource
import javax.inject.Inject

class MangaRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getTopMangas(query: Map<String, String>) = remoteDataSource.getTopMangas(query)
}