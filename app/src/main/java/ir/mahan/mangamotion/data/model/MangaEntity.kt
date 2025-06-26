package ir.mahan.mangamotion.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.mahan.mangamotion.utils.constants.MANGA_TABLE_NAME
import retrofit2.Response

@Entity(tableName = MANGA_TABLE_NAME)
data class MangaEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val responseManga: ResponseTopManga,
)