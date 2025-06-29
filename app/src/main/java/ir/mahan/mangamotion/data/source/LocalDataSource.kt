package ir.mahan.mangamotion.data.source

import ir.mahan.mangamotion.data.database.AnimeDao
import ir.mahan.mangamotion.data.database.MangaDao
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.model.anime.AnimeEntity
import ir.mahan.mangamotion.utils.constants.ANIME_NAME
import javax.inject.Inject
import javax.inject.Named

class LocalDataSource @Inject constructor(
    private val dao: MangaDao,
    @Named(ANIME_NAME) private val animeDao: AnimeDao
) {
    ///////////////////////////////////////////////////////////////////////////
    // Manga
    ///////////////////////////////////////////////////////////////////////////
    fun getMangaResponse(id: Int) = dao.getMangaResponse(id)
    fun checkMangaResponseExist(id: Int) = dao.checkMangaExist(id)
    suspend fun saveMangaResponse(mangaEntity: MangaEntity) =  dao.insertMangaResponse(mangaEntity)

    ///////////////////////////////////////////////////////////////////////////
    // Anime
    ///////////////////////////////////////////////////////////////////////////
    fun getAnimeResponse(id: Int) = animeDao.getAnimeResponse(id)
    fun checkAnimeResponseExist(id: Int) = animeDao.checkAnimeExist(id)
    suspend fun saveAnimeResponse(animeEntity: AnimeEntity) =  animeDao.insertAnimeResponse(animeEntity)
}