package ir.mahan.mangamotion.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.mahan.mangamotion.data.model.AuthInfo
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import ir.mahan.mangamotion.utils.constants.USER_STORE_NAME
import ir.mahan.mangamotion.utils.constants.USER_UID_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    //-- Data Store --
    private val appContext = context.applicationContext
    companion object {
        // Extension  property
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_STORE_NAME)
        private val USER_UID = stringPreferencesKey(USER_UID_KEY)
    }

    private suspend fun saveUIDToStore(uid: String){
        appContext.dataStore.edit {
            it[USER_UID] = uid
        }
    }

    val getUID: Flow<String?> = appContext.dataStore.data.map {
        it[USER_UID]
    }

    suspend fun clearUser() {
        appContext.dataStore.edit {
            it.clear()
        }
    }

    //-- FireBase --
    val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser)
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { com.google.firebase.Firebase.auth.removeAuthStateListener(listener) }
        }

    val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(authInfo: AuthInfo){
        Firebase.auth.createUserWithEmailAndPassword(authInfo.email, authInfo.password).await()
    }

    fun signUpWithCallback(authInfo: AuthInfo, onSuccess: (FirebaseUser) -> Unit, onFailure: (Exception) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(authInfo.email, authInfo.password)
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(DEBUG_TAG).d("createUserWithEmail:success")
                    val user = Firebase.auth.currentUser
                    onSuccess(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(DEBUG_TAG).d("createUserWithEmail:failure => ${task.exception}")
                    onFailure(task.exception!!)
                }
            }
    }

    fun signInWithCallback(authInfo: AuthInfo, onSuccess: (FirebaseUser) -> Unit, onFailure: (Exception) -> Unit){
        Firebase.auth.signInWithEmailAndPassword(authInfo.email, authInfo.password)
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(DEBUG_TAG).d("createUserWithEmail:success")
                    val user = Firebase.auth.currentUser
                    onSuccess(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(DEBUG_TAG).d("createUserWithEmail:failure => ${task.exception}")
                    onFailure(task.exception!!)
                }
            }
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }

    suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

}