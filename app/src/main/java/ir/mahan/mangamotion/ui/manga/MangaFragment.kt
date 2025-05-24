package ir.mahan.mangamotion.ui.manga

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.data.SessionManager
import ir.mahan.mangamotion.data.adapter.TopItemAdapter
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.databinding.FragmentMangaBinding
import ir.mahan.mangamotion.utils.constants.BASE_AVATAR_URL
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.setup
import ir.mahan.mangamotion.utils.smoothLoad
import ir.mahan.mangamotion.viewmodel.manga.MangaIntents
import ir.mahan.mangamotion.viewmodel.manga.MangaStates
import ir.mahan.mangamotion.viewmodel.manga.MangaViewModel
import ir.mahan.mangamotion.viewmodel.profile.ProfileViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MangaFragment : Fragment() {
    // Binding
    private var _binding: FragmentMangaBinding? = null
    val binding get() = _binding!!

    private val viewModel: MangaViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // Other
    @Inject lateinit var topItemsAdapter: TopItemAdapter
    @Inject lateinit var sessionManager: SessionManager

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
            //async { requestForTopMangas() }.await()
            manageStates()
        }
        binding.apply {
            val uid = sessionManager.currentUserId
            Timber.tag(DEBUG_TAG).d("UUID: $uid")
            appbarLay.avatarImg.smoothLoad(BASE_AVATAR_URL.plus(uid.hashCode()))
        }
        // UI
        setupRecyclerView()
    }

    private suspend fun manageStates()  {
        viewModel.states.collect{state->
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}