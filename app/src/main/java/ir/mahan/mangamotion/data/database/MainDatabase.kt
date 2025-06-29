package ir.mahan.mangamotion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.model.anime.AnimeEntity

@Database(entities = [MangaEntity::class, AnimeEntity::class],  version = 2,  exportSchema = false)
@TypeConverters(CustomTypeConverter::class)
abstract class MainDatabase : RoomDatabase(){
    abstract fun mangaDao(): MangaDao
    abstract fun animeDao(): AnimeDao
}