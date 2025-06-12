package ir.mahan.mangamotion.data.api

import ir.mahan.mangamotion.data.model.ResponseTopManga
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIEndpoints {
    @GET("top/manga")
    suspend fun getTopMangas(@QueryMap query: Map<String, String>): Response<ResponseTopManga>

    @GET("top/anime")
    suspend fun getTopAnime(@QueryMap query: Map<String, String>): Response<ResponseTopManga>

    @GET("manga")
    suspend fun searchManga(@QueryMap query: Map<String, String>): Response<ResponseTopManga>
}