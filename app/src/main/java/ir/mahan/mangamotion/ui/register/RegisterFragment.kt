package ir.mahan.mangamotion.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import coil.load
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.data.model.AuthInfo
import ir.mahan.mangamotion.databinding.FragmentRegisterBinding
import ir.mahan.mangamotion.utils.checkForEmailMatching
import ir.mahan.mangamotion.utils.toBitmap
import ir.mahan.mangamotion.viewmodel.register.RegisterIntents
import ir.mahan.mangamotion.viewmodel.register.RegisterStates
import ir.mahan.mangamotion.viewmodel.register.RegisterViewModel
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    // Binding
    private var _binding: FragmentRegisterBinding? = null
    val binding get() = _binding!!

    // other
    private val viewModel: RegisterViewModel by viewModels()
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Manage States
        lifecycleScope.launch { manageStates() }
        // Initialize Views
        binding.apply {
            headerImg.load(R.drawable.login)
            // Email
            emailTxtEdt.addTextChangedListener {
                email = it.toString()
                changeButtonState()
                if (it.toString().checkForEmailMatching()) {
                    emailTxtLay.error = ""
                } else {
                    emailTxtLay.error = "Invalid Email"
                }
            }
            // Password
            passTxtEdt.addTextChangedListener {
                password = it.toString()
                changeButtonState()
                if (isPasswordValid()) {
                    passTxtLay.error = ""
                } else {
                    passTxtLay.error = "Password must be at least 8 characters"
                }
            }
            // Sign In Button
            signInButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.intents.send(RegisterIntents.SignUpUser(AuthInfo(email, password)))
                }
            }

        }
    }

    private suspend fun manageStates() {
        viewModel.states.collect { state ->
            when (state) {
                RegisterStates.Idle -> {}
                RegisterStates.Loading -> {
                    binding.signInButton.startAnimation()
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
                is RegisterStates.SuccessfulLogin -> {
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_check)
                    binding.signInButton.doneLoadingAnimation(binding.signInButton.solidColor, drawable!!.toBitmap())
                    loginUser(user = state.user)
                }
                is RegisterStates.FailedLogin -> {
                    binding.signInButton.revertAnimation {
                        binding.signInButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_bg)
                    }
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loginUser(user: FirebaseUser) {
        Toast.makeText(requireContext(), "UUID: ${user.uid}", Toast.LENGTH_LONG).show()
        val action = RegisterFragmentDirections.actionToManga()
        findNavController().navigate(
            directions = action,
            navOptions = navOptions {
                popUpTo(R.id.splashFragment) {
                    inclusive = true
                }
            }
        )
    }

    private fun isPasswordValid() = binding.passTxtEdt.text.toString().length >= 8
    private fun isEmailValid() = binding.emailTxtEdt.text.toString().checkForEmailMatching()
    private fun changeButtonState() {
        binding.signInButton.isEnabled = isEmailValid() && isPasswordValid()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}