package ir.mahan.mangamotion.viewmodel.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.data.repository.MangaRepository
import ir.mahan.mangamotion.utils.NetworkObserver
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.constants.MangaScreenQueryMaps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val repository: MangaRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    val intents: Channel<MangaIntents> = Channel()
    private val _states = MutableStateFlow<MangaStates>(MangaStates.TopMangasLoading)
    val states get() = _states

    // Other Props
    private var topMangasCached = false
    private var doujinsCached = false
    private var manhuasCached = false
    private var manhwasCached = false


    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                MangaIntents.LoadTopMangas -> getTopMangas()
                MangaIntents.LoadNewMangas -> searchManga(
                    MangaScreenQueryMaps.CURRENTLY_PUBLISHING.queries,
                    intent
                )

                MangaIntents.LoadDoujins -> searchManga(
                    MangaScreenQueryMaps.DOUJINS.queries,
                    intent
                )

                MangaIntents.LoadManhwas -> searchManga(MangaScreenQueryMaps.MANHWA.queries, intent)
                MangaIntents.LoadManhuas -> searchManga(MangaScreenQueryMaps.MANHUA.queries, intent)
            }
        }
    }

    private fun getTopMangas() = viewModelScope.launch(Dispatchers.IO) {
        _states.value = MangaStates.TopMangasLoading
        topMangasCached = repository.checkMangaResponseExist(0).first()
        if (topMangasCached) {
            getMangasFromDB(0)
        } else {
            fetchTopMangasFromApi()
        }

    }

    private fun fetchTopMangasFromApi() = viewModelScope.launch {
        _states.value = try {
            Timber.tag(DEBUG_TAG).d("Calling API")
            val response = repository.fetchTopMangas(MangaScreenQueryMaps.POPULAR.queries)
            val wrappedResult = ResponseHandler(response).handleResponseCodes()
            if (wrappedResult.message != null)
                MangaStates.Error(wrappedResult.message.toString())
            else {
                cacheMangas(0, response.body()!!)
                MangaStates.ShowTopMangas(wrappedResult.data!!.data)
            }
        } catch (e: Exception) {
            MangaStates.Error(e.message.toString())
        }
    }

    private fun cacheMangas(id: Int, response: ResponseTopManga) = viewModelScope.launch {
        Timber.tag(DEBUG_TAG).d("Caching mangas")
        repository.saveMangaResponse(MangaEntity(id, response))
    }

    private fun getMangasFromDB(id: Int) = viewModelScope.launch {
        Timber.tag(DEBUG_TAG).d("Getting from DB")
        val response = repository.getMangasByDB(id).first().responseManga
        _states.value = MangaStates.ShowTopMangas(response.data)
        Timber.tag(DEBUG_TAG).d("Hello.....")
    }


    private fun searchManga(
        queryMap: Map<String, String>,
        intent: MangaIntents,
        dbId: Int? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
            _states.value = MangaStates.NewMangasLoading
            _states.value = try {
                val response = repository.searchManga(queryMap)
                val wrappedResult = ResponseHandler(response).handleResponseCodes()
                if (wrappedResult.message != null)
                    MangaStates.Error(wrappedResult.message.toString())
                else{
                    if (dbId != null)
                        cacheMangas(dbId, response.body()!!)
                    castStateBasedOnIntent(wrappedResult.data!!.data, intent)
                }
            } catch (e: Exception) {
                MangaStates.Error(e.message.toString())
            }
    }

    private fun getDoujins(queryMap: Map<String, String>, intent: MangaIntents) =
        viewModelScope.launch(Dispatchers.IO) {
            doujinsCached = repository.checkMangaResponseExist(2).first()
            if (doujinsCached) {
                getMangasFromDB(2)
            } else {
                searchManga(queryMap, intent)
            }
        }

    private fun castStateBasedOnIntent(
        result: List<ResponseTopManga.Data>,
        intent: MangaIntents
    ): MangaStates {
        return when (intent) {
            MangaIntents.LoadTopMangas -> MangaStates.ShowTopMangas(result)
            MangaIntents.LoadNewMangas -> MangaStates.ShowNewMangas(result)
            MangaIntents.LoadDoujins -> {
//                cacheMangas(2,  )
                MangaStates.ShowDoujins(result)
            }
            MangaIntents.LoadManhwas -> MangaStates.ShowManhwas(result)
            MangaIntents.LoadManhuas -> MangaStates.ShowManhuas(result)
        }
    }

}