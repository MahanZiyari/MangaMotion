package ir.mahan.mangamotion.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.utils.constants.MANGA_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangaResponse(entity: MangaEntity)

    @Update
    suspend fun updateMangaResponse(entity: MangaEntity)

    @Delete
    suspend fun removeMangaResponse(entity: MangaEntity)

    @Query("SELECT * FROM $MANGA_TABLE_NAME ORDER BY ID ASC")
    fun getAllMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM $MANGA_TABLE_NAME WHERE ID=:id")
    fun getMangaResponse(id: Int): Flow<MangaEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM $MANGA_TABLE_NAME WHERE ID = :id)")
    fun checkMangaExist(id: Int): Flow<Boolean>

}