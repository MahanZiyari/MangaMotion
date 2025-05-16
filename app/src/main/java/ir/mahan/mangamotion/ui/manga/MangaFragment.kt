package ir.mahan.mangamotion.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.data.adapter.TopItemAdapter
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.databinding.FragmentMangaBinding
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.setup
import ir.mahan.mangamotion.viewmodel.manga.MangaIntents
import ir.mahan.mangamotion.viewmodel.manga.MangaStates
import ir.mahan.mangamotion.viewmodel.manga.MangaViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MangaFragment : Fragment() {
    // Binding
    private var _binding: FragmentMangaBinding? = null
    val binding get() = _binding!!

    private val viewModel: MangaViewModel by viewModels()

    @Inject lateinit var topItemsAdapter: TopItemAdapter

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
        // Data Calling
        lifecycleScope.launch {
            Timber.tag(DEBUG_TAG).d("lifeCycleScope launched")
            async { requestForTopMangas() }.await()
            Timber.tag(DEBUG_TAG).d("After manageStates")
            manageStates()
        }
        // UI
        setupRecyclerView()
    }

    private suspend fun manageStates()  {
        viewModel.states.collect{state->
            Timber.tag(DEBUG_TAG).d("NewState: $state")
            when(state){
                is MangaStates.Idle -> {}
                is MangaStates.TopMangasLoading -> {
                    binding.popularMangaShimmer.showShimmer()
                }
                is MangaStates.ShowTopMangas -> showTopMangas(state.topMangas)
                is MangaStates.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.popularMangaShimmer.setup(
            newAdapter = topItemsAdapter,
            newLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL,false)
        )
    }

    private fun showTopMangas(topMangas: List<ResponseTopManga.Data>) {
        Timber.tag(DEBUG_TAG).d("Data Received: \n${topMangas.size}")
        binding.popularMangaShimmer.hideShimmer()
        topItemsAdapter.setItems(topMangas)
    }

    private fun requestForTopMangas() = lifecycleScope.launch {
        Timber.tag(DEBUG_TAG).d("Requesting for Top Mangas")
        viewModel.intents.send(MangaIntents.LoadTopMangas)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}