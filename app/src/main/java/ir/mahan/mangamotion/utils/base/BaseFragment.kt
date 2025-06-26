package ir.mahan.mangamotion.utils.base


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.mangamotion.utils.NetworkObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var networkObserver: NetworkObserver

    //Other
    protected var isNetworkAvailable = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Check network
        lifecycleScope.launch {
            networkObserver.observeNetworkStatus().collect {
                isNetworkAvailable = it
                if (!it) {
                    Toast.makeText(requireContext(), "Check Your Network!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}