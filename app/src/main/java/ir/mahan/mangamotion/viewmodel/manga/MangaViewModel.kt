package ir.mahan.mangamotion.viewmodel.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.model.MangaEntity
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.data.repository.MangaRepository
import ir.mahan.mangamotion.utils.NetworkObserver
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.MangaScreenQueryMaps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val repository: MangaRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    val intents: Channel<MangaIntents> = Channel(5)
    private val _states = MutableStateFlow<MangaStates>(MangaStates.TopMangasLoading)
    val states get() = _states

    // Other Props
    private var topMangasCached = false
    private var isResponseCached = false
    private val intToIntentMap = (0..4)
        .associateWith {
            when (it) {
                0 -> MangaIntents.LoadTopMangas
                1 -> MangaIntents.LoadNewMangas
                2 -> MangaIntents.LoadDoujins
                3 -> MangaIntents.LoadManhwas
                4 -> MangaIntents.LoadManhuas
                else -> MangaIntents.LoadTopMangas
            }
        }

    init {
        manageIntents()
    }

    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                // id = 0
                MangaIntents.LoadTopMangas -> getTopMangas()
                // id = 1
                MangaIntents.LoadNewMangas -> searchManga(
                    MangaScreenQueryMaps.CURRENTLY_PUBLISHING.queries,
                    intent
                )
                // id = 2
                MangaIntents.LoadDoujins -> retrieveManga(
                    MangaScreenQueryMaps.DOUJINS.queries,
                    intent,
                    2
                )
                // id  = 3
                MangaIntents.LoadManhwas -> retrieveManga(
                    MangaScreenQueryMaps.MANHWA.queries,
                    intent,
                    3
                )
                // id = 4
                MangaIntents.LoadManhuas -> searchManga(
                    MangaScreenQueryMaps.MANHUA.queries,
                    intent,
                    4
                )
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Top Manga Functions
    ///////////////////////////////////////////////////////////////////////////
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
//            Timber.tag(DEBUG_TAG).d("Calling API")
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

    ///////////////////////////////////////////////////////////////////////////
    // Search Manga Functions
    ///////////////////////////////////////////////////////////////////////////

    private fun searchManga(
        queryMap: Map<String, String>,
        intent: MangaIntents,
        dbId: Int? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
            _states.value = MangaStates.NewMangasLoading
            _states.value = try {
                val response = repository.searchManga(queryMap)
                val wrappedResult = ResponseHandler(response).handleResponseCodes()
                if (wrappedResult.message != null) {
                    if (wrappedResult.message.contains("429")) {
                        handleTooManyRequests(queryMap, intent, dbId)
                    }
                    MangaStates.Error(wrappedResult.message.toString())
                } else {
                    // Successful response from API
                    if (dbId != null)
                        cacheMangas(dbId, response.body()!!)
                    castStateBasedOnIntent(wrappedResult.data!!.data, intent)
                }
            } catch (e: Exception) {
                MangaStates.Error(e.message.toString())
            }
    }

    private fun retrieveManga(queryMap: Map<String, String>, intent: MangaIntents, dbId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            isResponseCached = repository.checkMangaResponseExist(dbId).first()
            if (isResponseCached) {
                getMangasFromDB(dbId)
            } else {
                searchManga(queryMap, intent, dbId)
            }
        }

    ///////////////////////////////////////////////////////////////////////////
    // General Functions
    ///////////////////////////////////////////////////////////////////////////

    private suspend fun handleTooManyRequests(
        queryMap: Map<String, String>,
        intent: MangaIntents,
        dbId: Int? = null
    ) {
        delay(500)
        searchManga(queryMap, intent, dbId)
    }

    private fun getMangasFromDB(id: Int) = viewModelScope.launch {
        val response = repository.getMangasByDB(id).first().responseManga
        _states.value = castStateBasedOnIntent(response.data, intToIntentMap[id]!!)
    }

    private fun cacheMangas(id: Int, response: ResponseTopManga) = viewModelScope.launch {
        repository.saveMangaResponse(MangaEntity(id, response))
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