package ir.mahan.mangamotion.ui.register

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.lifecycleScope
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.databinding.FragmentRegisterBinding
import ir.mahan.mangamotion.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    // Binding
    private var _binding: FragmentRegisterBinding? = null
    val binding get() = _binding!!

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
        binding.apply {
            headerImg.load(R.drawable.login)
            //passTxtLay.endIconDrawable = getDrawable(requireContext(), R.drawable.eye)
        }
    }

    fun getThemeAttribute(context: Context, attributeResId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attributeResId, typedValue, true)
        return typedValue.data
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}