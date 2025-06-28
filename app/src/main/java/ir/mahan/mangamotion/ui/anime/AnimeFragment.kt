package ir.mahan.mangamotion.ui.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList
import ir.mahan.mangamotion.databinding.FragmentAnimeBinding
import ir.mahan.mangamotion.ui.anime.adapter.AnimeListAdapter
import ir.mahan.mangamotion.utils.base.BaseFragment
import ir.mahan.mangamotion.utils.constants.BASE_AVATAR_URL
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.setup
import ir.mahan.mangamotion.utils.smoothLoad
import ir.mahan.mangamotion.viewmodel.anime.AnimeIntents
import ir.mahan.mangamotion.viewmodel.anime.AnimeStates
import ir.mahan.mangamotion.viewmodel.anime.AnimeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AnimeFragment : BaseFragment() {
    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    // Binding
    private var _binding: FragmentAnimeBinding? = null
    val binding get() = _binding!!
    // Properties
    private val viewModel by viewModels<AnimeViewModel>()
    @Inject
    lateinit var sessionManager: SessionManager
    //Adapters
    @Inject
    lateinit var topAnimeAdapter: AnimeListAdapter


    ///////////////////////////////////////////////////////////////////////////
    // Behavior Functions
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Called inside OnViewCreated to handle UI  and Binding related operations
     */
    private fun handleUI() = binding.apply {
        val uid = sessionManager.currentUserId
        animeAppbarLay.avatarImg.smoothLoad(BASE_AVATAR_URL.plus(uid.hashCode()))
        // UI
        handleNetworkStatus()
//        initializeNewItemsRecyclerView()
    }


    private fun displayTopAnimeList(animeList: List<ResponseAnimeList.Data>) {
//        Timber.tag(DEBUG_TAG).d("Top Anime Received: ${animeList.size} items")
        topAnimeAdapter.setItems(animeList)
        binding.topAnimeSec.topAnimeList.apply {
            adapter = topAnimeAdapter
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
            setIntervalRatio(0.6f)
        }
        binding.topAnimeSec.topAnimeLoading.isVisible = false
        binding.topAnimeSec.topAnimeCarousel.isVisible = true
//        callSubSections()
    }

    private fun initializeNewItemsRecyclerView()  {
        /*Timber.tag(DEBUG_TAG).d("setupNewItemsRecycler")
        binding.newMangaLay.mangaList.setup(
            newAdapter = newItemsAdapter,
            newLayoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        newItemsAdapter.changeBindMode(false)*/
    }

    private fun handleNetworkStatus() = binding.apply {
        if (!isNetworkAvailable) {
            animeContentLay.isVisible = false
            noNetLay.isVisible = true
        } else {
            animeContentLay.isVisible = true
            noNetLay.isVisible = false
        }
    }

    private suspend fun manageStates() {
        viewModel.states.collect { state ->
            when(state){
                // Top Anime
                is AnimeStates.LoadingForTopAnimeList -> {
                    binding.topAnimeSec.topAnimeLoading.isVisible = true
                    binding.topAnimeSec.topAnimeCarousel.isVisible = false
                }
                is AnimeStates.ShowTopAnimeList -> displayTopAnimeList(state.animeList)
                is AnimeStates.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle Methods
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                manageStates()
            }
        }
        lifecycleScope.launch {
//            delay(1000)
            viewModel.intents.send(AnimeIntents.LoadTopAnimeList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}