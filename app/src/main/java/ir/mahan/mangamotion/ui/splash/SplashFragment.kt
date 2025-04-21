package ir.mahan.mangamotion.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.databinding.FragmentSplashBinding
import ir.mahan.mangamotion.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    // Binding
    private var _binding: FragmentSplashBinding? = null
    val binding get() = _binding

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            // Check if user is signed in
            viewModel.intents.send(SplashIntents.CheckUser)
            // Delay for 2 seconds
            delay(2500)
            // Manage States
            manageStates()
        }
    }

    suspend fun manageStates() {
        viewModel.states.collect {state ->
            when(state) {
                SplashStates.Idle -> {}
                SplashStates.NavigateToHome -> {
                    findNavController().run {
                        popBackStack(R.id.splashFragment, true)
                        navigate(R.id.action_to_manga)
                    }
                }
                SplashStates.NavigateToLogin -> {
                    findNavController().run {
                        popBackStack(R.id.splashFragment, true)
                        navigate(R.id.action_to_register)
                    }
                }
            }


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}