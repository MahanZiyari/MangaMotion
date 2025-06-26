package ir.mahan.mangamotion.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NetworkObserver @Inject constructor(
    private val networkRequest: NetworkRequest,
    private val connectivityManager: ConnectivityManager
) : ConnectivityManager.NetworkCallback() {

    // properties
    private val isNetworkAvailable = MutableStateFlow(false)
    private var capabilities: NetworkCapabilities? = null

    // Methods
    fun observeNetworkStatus(): MutableStateFlow<Boolean> {
        // Register callback
        connectivityManager.registerNetworkCallback(networkRequest, this)
        // check fot network based on os version
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }
        capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (capabilities == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }
        isNetworkAvailable.value = when {
            capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}