package ir.mahan.mangamotion.data.model.anime

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.mahan.mangamotion.utils.constants.ANIME_TABLE_NAME

@Entity(tableName = ANIME_TABLE_NAME)
data class AnimeEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val responseAnime: ResponseAnimeList,
)