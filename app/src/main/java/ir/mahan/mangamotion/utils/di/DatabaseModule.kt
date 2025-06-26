package ir.mahan.mangamotion.utils.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.mahan.mangamotion.data.database.MainDatabase
import ir.mahan.mangamotion.utils.constants.MAIN_DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        MainDatabase::class.java,
        MAIN_DB_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()


    @Provides
    @Singleton
    fun provideDao(database: MainDatabase) = database.dao()

}