package ir.mahan.mangamotion.data.source

import ir.mahan.mangamotion.data.api.APIEndpoints
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val endPoints: APIEndpoints) {
    suspend fun getTopMangas(query: Map<String, String>) = endPoints.getTopMangas(query)
    suspend fun getTopAnime(query: Map<String, String>) = endPoints.getTopMangas(query)
    suspend fun searchManga(query: Map<String, String>) = endPoints.searchManga(query)
}