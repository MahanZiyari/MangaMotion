package ir.mahan.mangamotion.viewmodel.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.data.repository.MangaRepository
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.constants.MangaScreenQueryMaps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val repository: MangaRepository) : ViewModel() {

    val intents: Channel<MangaIntents> = Channel()
    private val _states = MutableStateFlow<MangaStates>(MangaStates.TopMangasLoading)
    val states get() = _states


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
        _states.value = try {
            val response = repository.getTopMangas(MangaScreenQueryMaps.POPULAR.queries)
            val wrappedResult = ResponseHandler(response).handleResponseCodes()
            if (wrappedResult.message != null)
                MangaStates.Error(wrappedResult.message.toString())
            else
                MangaStates.ShowTopMangas(wrappedResult.data!!.data)
        } catch (e: Exception) {
            MangaStates.Error(e.message.toString())
        }
    }

    private fun searchManga(queryMap: Map<String, String>, intent: MangaIntents) =
        viewModelScope.launch(Dispatchers.IO) {
            _states.value = MangaStates.NewMangasLoading
            _states.value = try {
                val result = repository.searchManga(queryMap)
                val wrappedResult = ResponseHandler(result).handleResponseCodes()
                if (wrappedResult.message != null)
                    MangaStates.Error(wrappedResult.message.toString())
                else
                    castStateBasedOnIntent(wrappedResult.data!!.data, intent)
            } catch (e: Exception) {
                MangaStates.Error(e.message.toString())
            }
        }

    private fun castStateBasedOnIntent(
        result: List<ResponseTopManga.Data>,
        intent: MangaIntents
    ): MangaStates {
        return when (intent) {
            MangaIntents.LoadTopMangas -> MangaStates.ShowTopMangas(result)
            MangaIntents.LoadNewMangas -> MangaStates.ShowNewMangas(result)
            MangaIntents.LoadDoujins -> MangaStates.ShowDoujins(result)
            MangaIntents.LoadManhwas -> MangaStates.ShowManhwas(result)
            MangaIntents.LoadManhuas -> MangaStates.ShowManhuas(result)
        }
    }

}