package ir.mahan.mangamotion.data.source

import ir.mahan.mangamotion.data.api.APIEndpoints
import ir.mahan.mangamotion.data.database.MainDao
import ir.mahan.mangamotion.data.model.MangaEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MainDao) {
    suspend fun getMangaResponse(id: Int) = dao.getMangaResponse(id)
    suspend fun checkMangaResponseExist(id: Int) = dao.checkMangaExist(id)
    suspend fun saveMangaResponse(mangaEntity: MangaEntity) =  dao.insertMangaResponse(mangaEntity)
}