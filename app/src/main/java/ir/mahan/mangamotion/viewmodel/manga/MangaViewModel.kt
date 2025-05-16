package ir.mahan.mangamotion.viewmodel.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.mangamotion.data.repository.MangaRepository
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.ResponseHandler
import ir.mahan.mangamotion.utils.constants.APIQueryParameters
import ir.mahan.mangamotion.utils.constants.LIMIT_NUMBER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val repository: MangaRepository) :
    ViewModel() {

    val intents: Channel<MangaIntents> = Channel()
    private val _states = MutableStateFlow<MangaStates>(MangaStates.Idle)
    val states get() = _states

    // Properties
    private val topMangaQuery = buildMap<String, String> {
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }

    init {
        manageIntents()
    }


    private fun manageIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                MangaIntents.LoadTopMangas -> getTopMangas()
            }
        }
    }

    private fun getTopMangas() = viewModelScope.launch(Dispatchers.IO) {
        Timber.tag(DEBUG_TAG).d("Start loading top mangas")
        _states.value = MangaStates.TopMangasLoading
        _states.value = try {
            Timber.tag(DEBUG_TAG).d("In try block")
            val result = repository.getTopMangas(topMangaQuery)
            val wrappedResult = ResponseHandler(result).handleResponseCodes()
            Timber.tag(DEBUG_TAG).d("Result: ${wrappedResult.data!!.data.size} item")
            if (wrappedResult.message != null)
                MangaStates.Error(wrappedResult.message.toString())
            else
                MangaStates.ShowTopMangas(wrappedResult.data!!.data)
        } catch (e: Exception) {
            MangaStates.Error(e.message.toString())
        }
    }

}