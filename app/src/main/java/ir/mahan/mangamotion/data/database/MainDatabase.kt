package ir.mahan.mangamotion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.mahan.mangamotion.data.model.MangaEntity

@Database(entities = [MangaEntity::class],  version = 1,  exportSchema = false)
@TypeConverters(CustomTypeConverter::class)
abstract class MainDatabase : RoomDatabase(){
    abstract fun dao(): MainDao
}