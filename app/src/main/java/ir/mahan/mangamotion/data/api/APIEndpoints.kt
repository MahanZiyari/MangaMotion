package ir.mahan.mangamotion.data.api

import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIEndpoints {
    @GET("top/manga")
    suspend fun getTopMangas(@QueryMap query: Map<String, String>): Response<ResponseTopManga>

    @GET("top/anime")
    suspend fun getTopAnime(@QueryMap query: Map<String, String>): Response<ResponseAnimeList>

    @GET("manga")
    suspend fun searchManga(@QueryMap query: Map<String, String>): Response<ResponseTopManga>

    @GET("anime")
    suspend fun searchAnime(@QueryMap query: Map<String, String>): Response<ResponseAnimeList>
}