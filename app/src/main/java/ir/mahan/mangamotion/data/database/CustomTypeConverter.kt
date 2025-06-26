package ir.mahan.mangamotion.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import ir.mahan.mangamotion.data.model.ResponseTopManga

class CustomTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun mangaToJson(responseManga: ResponseTopManga): String {
        return gson.toJson(responseManga)
    }

    @TypeConverter
    fun stringToRecipe(data: String): ResponseTopManga {
        return gson.fromJson(data, ResponseTopManga::class.java)
    }




}