package ir.mahan.mangamotion.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.mahan.mangamotion.R
import ir.mahan.mangamotion.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Binding
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    // Nav Host
    private lateinit var  navHostFragment : NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment  = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHostFragment.navController
        binding.apply {
            bottomNav.setupWithNavController(navController)
            navController.addOnDestinationChangedListener{ _, destination, _ ->
                when(destination.id){
                    R.id.splashFragment -> setBottomAppBarVisibility(false)
                    R.id.registerFragment -> setBottomAppBarVisibility(false)
                    R.id.settingsFragment -> setBottomAppBarVisibility(false)
                    else -> setBottomAppBarVisibility(true)
                }
            }
        }
    }

    // Hide Bottom App Bar
    private fun setBottomAppBarVisibility(isVisibility: Boolean) {
        binding.apply {
            if (isVisibility) {
                mainAppBar.isVisible = true
                searchFab.isVisible = true
            } else {
                mainAppBar.isVisible = false
                searchFab.isVisible = false
            }
        }
    }

    //Calligraphy
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}