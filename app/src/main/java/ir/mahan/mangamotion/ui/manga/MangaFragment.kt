package ir.mahan.mangamotion.ui.manga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.data.adapter.TopItemAdapter
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.databinding.FragmentMangaBinding
import ir.mahan.mangamotion.utils.RememberRecyclerView
import ir.mahan.mangamotion.utils.base.BaseFragment
import ir.mahan.mangamotion.utils.constants.BASE_AVATAR_URL
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.setup
import ir.mahan.mangamotion.utils.smoothLoad
import ir.mahan.mangamotion.viewmodel.manga.MangaIntents
import ir.mahan.mangamotion.viewmodel.manga.MangaStates
import ir.mahan.mangamotion.viewmodel.manga.MangaViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MangaFragment : BaseFragment() {
    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    // Binding
    private var _binding: FragmentMangaBinding? = null
    val binding get() = _binding!!
    // ViewModels
    private val viewModel: MangaViewModel by activityViewModels()

    // Adapters
    @Inject
    lateinit var topItemsAdapter: TopItemAdapter
    @Inject
    lateinit var newItemsAdapter: TopItemAdapter
    @Inject
    lateinit var doujinsAdapter: TopItemAdapter
    @Inject
    lateinit var manhwasAdapter: TopItemAdapter
    @Inject
    lateinit var manhuasAdapter: TopItemAdapter

    // Properties
    @Inject lateinit var sessionManager: SessionManager


    ///////////////////////////////////////////////////////////////////////////
    // Behavior Functions
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Called inside OnViewCreated to handle UI  and Binding related operations
     */
    private fun handleUI() = binding.apply {
        val uid = sessionManager.currentUserId
        appbarLay.avatarImg.smoothLoad(BASE_AVATAR_URL.plus(uid.hashCode()))
        // UI
        handleNetworkStatus()
        initTopRecyclerView()
        initializeNewItemsRecyclerView()
    }

    private fun handleNetworkStatus() = binding.apply {
        if (!isNetworkAvailable) {
            contentLay.isVisible = false
            noNetLay.isVisible = true
        } else {
            contentLay.isVisible = true
            noNetLay.isVisible = false
        }
    }


    private suspend fun manageStates()  {
        viewModel.states.collect { state ->
            when(state){
                is MangaStates.Idle -> {}
                is MangaStates.TopMangasLoading -> {
                    binding.popularMangaShimmer.showShimmer()
                }
                is MangaStates.ShowNewMangas -> showNewMangas(state.newMangas)
                is MangaStates.ShowTopMangas -> showTopMangas(state.topMangas)
                is MangaStates.NewMangasLoading -> {}
                is MangaStates.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is MangaStates.ShowDoujins -> showDoujins(state.doujins)
                is MangaStates.ShowManhwas -> showKoreanComics(state.manhwas)
                is MangaStates.ShowManhuas -> showChineseComics(state.manhuas)
            }
        }
    }

    private fun showTopMangas(topMangas: List<ResponseTopManga.Data>) {
//        Timber.tag(DEBUG_TAG).d("Top Mangas Received")
        topItemsAdapter.setItems(topMangas)
        binding.popularMangaShimmer.hideShimmer()
        callFragmentIntents()
    }

    private fun showNewMangas(newMangas: List<ResponseTopManga.Data>) {
//        Timber.tag(DEBUG_TAG).d("New Mangas Received")
        newItemsAdapter.setItems(newMangas)
        initializeNewItemsRecyclerView()
    }

    private fun showDoujins(doujins: List<ResponseTopManga.Data>) {
//        Timber.tag(DEBUG_TAG).d("Doujins Received")
        if (binding.doujinLay.parent != null) {
            initLazyRecyclerViews(binding.doujinLay, doujins)
        }
    }

    private fun showKoreanComics(manhwas: List<ResponseTopManga.Data>) {
//        Timber.tag(DEBUG_TAG).d("Manhwas Received")
        if (binding.manhwaLay.parent != null) {
            initLazyRecyclerViews(binding.manhwaLay, manhwas)
        }
    }

    private fun showChineseComics(manhuas: List<ResponseTopManga.Data>) {
//        Timber.tag(DEBUG_TAG).d("Manhwas Received")
        if (binding.manhuaLay.parent != null) {
            initLazyRecyclerViews(binding.manhuaLay, manhuas)
        }
//        Timber.tag(DEBUG_TAG).d("new adapter: ${newItemsAdapter.itemCount}")
    }

    private fun callFragmentIntents() = lifecycleScope.launch {
        viewModel.intents.send(MangaIntents.LoadDoujins)
//        delay(1000)
        viewModel.intents.send(MangaIntents.LoadManhwas)
//        delay(800)
        viewModel.intents.send(MangaIntents.LoadManhuas)
    }

    private fun initTopRecyclerView() {
//        Timber.tag(DEBUG_TAG).d("Setting up popular recycler")
        binding.popularMangaShimmer.setup(
            newAdapter = topItemsAdapter,
            newLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL,false)
        )
    }

    private fun initializeNewItemsRecyclerView()  {
//        Timber.tag(DEBUG_TAG).d("setupNewItemsRecycler")
        binding.newMangaLay.mangaList.setup(
            newAdapter = newItemsAdapter,
            newLayoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        newItemsAdapter.changeBindMode(false)
    }

    private fun initLazyRecyclerViews(viewStub: ViewStub, items: List<ResponseTopManga.Data>)  {
        val inflated = viewStub.inflate()
        val recyclerView = when(viewStub.id)  {
            R.id.doujinLay -> inflated.findViewById<RememberRecyclerView>(R.id.doujinsList)
            R.id.manhwaLay -> inflated.findViewById<RememberRecyclerView>(R.id.manhwaList)
            R.id.manhuaLay -> inflated.findViewById<RememberRecyclerView>(R.id.manhuaList)
            else -> null
        }
        val adapter = when(viewStub.id)  {
            R.id.doujinLay -> doujinsAdapter
            R.id.manhwaLay -> manhwasAdapter
            R.id.manhuaLay -> manhuasAdapter
            else -> null
        }
        adapter?.setItems(items)
        adapter!!.changeBindMode(false)
        recyclerView?.setup(
            newAdapter = adapter,
            newLayoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
    }




    ///////////////////////////////////////////////////////////////////////////
    // LifeCycle Methods
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMangaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI()
        // Data Calling

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                manageStates()
            }
        }
        lifecycleScope.launch {
            viewModel.intents.send(MangaIntents.LoadNewMangas)
            viewModel.intents.send(MangaIntents.LoadTopMangas)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}