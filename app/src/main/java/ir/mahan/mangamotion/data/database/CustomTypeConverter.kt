package ir.mahan.mangamotion.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList

class CustomTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun mangaToJson(responseManga: ResponseTopManga): String {
        return gson.toJson(responseManga)
    }

    @TypeConverter
    fun animeToJson(responseAnimeList: ResponseAnimeList): String {
        return gson.toJson(responseAnimeList)
    }

    @TypeConverter
    fun stringToManga(data: String): ResponseTopManga {
        return gson.fromJson(data, ResponseTopManga::class.java)
    }

    @TypeConverter
    fun stringToAnime(data: String): ResponseAnimeList {
        return gson.fromJson(data, ResponseAnimeList::class.java)
    }


}