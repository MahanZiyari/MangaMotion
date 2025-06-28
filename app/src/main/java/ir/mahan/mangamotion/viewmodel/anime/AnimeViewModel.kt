package ir.mahan.mangamotion.viewmodel.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList
import ir.mahan.mangamotion.data.repository.AnimeRepository
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.AnimeScreenQueryMaps
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
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



    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                AnimeIntents.LoadTopAnimeList -> getTopAnimeList()
            }
        }
    }

    private fun getTopAnimeList() = viewModelScope.launch(Dispatchers.IO) {
        _states.value = AnimeStates.LoadingForTopAnimeList
        /*topMangasCached = repository.checkMangaResponseExist(0).first()
        if (topMangasCached) {
            getMangasFromDB(0)
        } else {
            fetchTopMangasFromApi()
        }*/
        fetchTopAnimeListFromApi()
    }

    private fun fetchTopAnimeListFromApi() = viewModelScope.launch {
        _states.value = try {
            Timber.tag(DEBUG_TAG).d("Calling API")
            val response = repository.fetchTopAnimeList(AnimeScreenQueryMaps.TOP.queries)
            val wrappedResult = ResponseHandler(response).handleResponseCodes()
            if (wrappedResult.message != null)
                AnimeStates.Error(wrappedResult.message.toString())
            else {
                cacheAnimeList(0, response.body()!!)
                AnimeStates.ShowTopAnimeList(wrappedResult.data!!.data!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AnimeStates.Error(e.message.toString())
        }
    }

    private fun cacheAnimeList(id: Int, response: ResponseAnimeList) = viewModelScope.launch {

    }

    private fun getAnimeListFromDB(id: Int) = viewModelScope.launch {

    }


}