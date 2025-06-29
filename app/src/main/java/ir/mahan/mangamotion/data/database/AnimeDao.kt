package ir.mahan.mangamotion.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.model.anime.AnimeEntity
import ir.mahan.mangamotion.utils.constants.ANIME_TABLE_NAME
import ir.mahan.mangamotion.utils.constants.MANGA_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeResponse(entity: AnimeEntity)

    @Update
    suspend fun updateAnimeResponse(entity: AnimeEntity)

    @Delete
    suspend fun removeAnimeResponse(entity: AnimeEntity)

    @Query("SELECT * FROM $ANIME_TABLE_NAME ORDER BY ID ASC")
    fun getFullAnimeList(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM $ANIME_TABLE_NAME WHERE ID=:id")
    fun getAnimeResponse(id: Int): Flow<AnimeEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM $ANIME_TABLE_NAME WHERE ID = :id)")
    fun checkAnimeExist(id: Int): Flow<Boolean>

}