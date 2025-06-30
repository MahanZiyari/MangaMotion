package ir.mahan.mangamotion.viewmodel.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.model.anime.AnimeEntity
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList
import ir.mahan.mangamotion.data.repository.AnimeRepository
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.AnimeScreenQueryMaps
import ir.mahan.mangamotion.utils.constants.AnimeScreenQueryMaps.FOR_KIDS
import ir.mahan.mangamotion.utils.constants.AnimeScreenQueryMaps.LATEST
import ir.mahan.mangamotion.utils.constants.AnimeScreenQueryMaps.OVA
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {

    val intents: Channel<AnimeIntents> = Channel()
    private val _states = MutableStateFlow<AnimeStates>(AnimeStates.LoadingForTopAnimeList)
    val states get() = _states

    // Other Props
    private var topAnimeCached = false
    private var isResponseCached = false

    private val intToIntentMap = (0..3)
        .associateWith {
            when (it) {
                0 -> AnimeIntents.LoadTopAnimeList
                1 -> AnimeIntents.LoadNewAnimeList
                2 -> AnimeIntents.LoadOvaAnimeList
                3 -> AnimeIntents.LoadKidsAnimeList
                else -> AnimeIntents.LoadTopAnimeList
            }
        }



    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                // id = 0
                AnimeIntents.LoadTopAnimeList -> getTopAnimeList()
                // id = 1
                AnimeIntents.LoadNewAnimeList -> searchAnime(LATEST.queries, intent)
                // id = 2
                AnimeIntents.LoadOvaAnimeList -> retrieveAnime(OVA.queries, intent, 2)
                // id = 3
                AnimeIntents.LoadKidsAnimeList -> retrieveAnime(FOR_KIDS.queries, intent, 3)
            }
        }
    }

    private fun getTopAnimeList() = viewModelScope.launch(Dispatchers.IO) {
        _states.value = AnimeStates.LoadingForTopAnimeList
        topAnimeCached = repository.checkAnimeResponseExist(0).first()
        if (topAnimeCached) {
            Timber.tag(DEBUG_TAG).d("Top Anime List is cached")
            getAnimeListFromDB(0)
        } else {
            fetchTopAnimeListFromApi()
        }
    }

    private fun fetchTopAnimeListFromApi() = viewModelScope.launch {
        _states.value = try {
            Timber.tag(DEBUG_TAG).d("Calling Top Anime API")
            val response = repository.fetchTopAnimeList(AnimeScreenQueryMaps.TOP.queries)
            if (response.code() == 429) {
                handleTooManyRequest(true)
                return@launch
            }
            val wrappedResult = ResponseHandler(response).handleResponseCodes()
            if (wrappedResult.message != null){
                /*if (wrappedResult.message.contains("429")){
                    handleTooManyRequest(true)
                }*/
                AnimeStates.Error(wrappedResult.message.toString())
            }
            else {
//                cacheAnimeList(0, response.body()!!)
                AnimeStates.ShowTopAnimeList(wrappedResult.data!!.data!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AnimeStates.Error(e.message.toString())
        }
    }

    private fun searchAnime(
        queryMap: Map<String, String>,
        intent: AnimeIntents,
        dbId: Int? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        Timber.tag(DEBUG_TAG).d("Calling Search Anime API")
//        _states.value = AnimeStates.LoadingForNewAnimeList
//        delay(1000)
        _states.value = try {
            val response = repository.searchAnime(queryMap)
            if (response.code() == 429) {
                handleTooManyRequest(false, intent, queryMap, dbId)
//                return@launch
            }
            val wrappedResult = ResponseHandler(response).handleResponseCodes()
            if (wrappedResult.message != null) {
                AnimeStates.Error(wrappedResult.message.toString())
            } else {
                // Successful response from API
                if (dbId != null)
                    cacheAnimeList(dbId, response.body()!!)
                castStateBasedOnIntent(wrappedResult.data!!.data!!, intent)
            }
        } catch (e: Exception) {
            AnimeStates.Error(e.message.toString())
        }
    }

    private suspend fun handleTooManyRequest(
        isTop: Boolean,
        intents: AnimeIntents = AnimeIntents.LoadTopAnimeList,
        queryMap: Map<String, String>? = null,
        dbId: Int? = null
    ) {
        Timber.tag(DEBUG_TAG).d("Handling Too Many Requests: ${intents}")
        delay(500)
        if (isTop)
            fetchTopAnimeListFromApi()
        else {
            searchAnime(queryMap!!, intents, dbId)
        }
    }

    private fun retrieveAnime(queryMap: Map<String, String>, intent: AnimeIntents, dbId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            //isResponseCached = repository.checkAnimeResponseExist(dbId).first()
            if (isResponseCached) {
                Timber.tag(DEBUG_TAG).d("Anime List with ID:$dbId is cached")
                getAnimeListFromDB(dbId)
            } else {
                searchAnime(queryMap, intent, dbId)
            }
        }

    private fun getAnimeListFromDB(id: Int) = viewModelScope.launch {
        Timber.tag(DEBUG_TAG).d("Getting Anime List from DB:  ID = $id")
        val response = repository.getAnimeResponseByDB(id).first().responseAnime
        _states.value = castStateBasedOnIntent(response.data!!, intToIntentMap[id]!!)
    }

    private fun cacheAnimeList(id: Int, response: ResponseAnimeList) = viewModelScope.launch {
        Timber.tag(DEBUG_TAG).d("Caching Anime to DB:  ID = $id")
        repository.saveAnimeResponse(AnimeEntity(id, response))
    }


    private fun castStateBasedOnIntent(
        result: List<ResponseAnimeList.Data>,
        intent: AnimeIntents
    ): AnimeStates = when (intent) {
        AnimeIntents.LoadTopAnimeList -> AnimeStates.ShowTopAnimeList(result)
        AnimeIntents.LoadNewAnimeList -> AnimeStates.ShowNewAnimeList(result)
        AnimeIntents.LoadOvaAnimeList -> AnimeStates.ShowOvaAnimeList(result)
        AnimeIntents.LoadKidsAnimeList -> AnimeStates.ShowKidsAnimeList(result)
    }


}