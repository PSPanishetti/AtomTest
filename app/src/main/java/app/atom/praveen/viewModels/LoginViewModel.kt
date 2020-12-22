package app.atom.praveen.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.atom.praveen.apiHelpers.FirebaseDBHelper
import app.atom.praveen.apiHelpers.GoogleSignInHelper
import app.atom.praveen.interfaces.FirebaseAuthCallback
import app.atom.praveen.interfaces.UserDetailsCallback
import app.atom.praveen.models.ModelUser
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

class LoginViewModel() : ViewModel() {

    private var modelUser: MutableLiveData<ModelUser>? = null
    private var firebaseDBHelper: FirebaseDBHelper? = null
    private var googleSingInHelper : GoogleSignInHelper? = null
    private var isLoading : MutableLiveData<Pair<Boolean, String>> ? =null
    private var isErrorOccurred : MutableLiveData<Pair<Boolean, String>> ? = null

    fun initializeModel()
    {
        if(modelUser==null){

            firebaseDBHelper= FirebaseDBHelper

            googleSingInHelper=GoogleSignInHelper(object : FirebaseAuthCallback{
                override fun onSuccess(firebaseUser: FirebaseUser?) {
                    getUserDataFromFirebaseDB(firebaseUser)
                }
                override fun onFailed(error: String) {
                    isErrorOccurred?.value = Pair(true,"Authentication Failed")
                }
            })

            isErrorOccurred =  MutableLiveData<Pair<Boolean,String>>()

            isLoading = MutableLiveData<Pair<Boolean,String>>()

            modelUser = MutableLiveData<ModelUser>()

        }
    }

    private fun getUserDataFromFirebaseDB(firebaseUser: FirebaseUser?) {
        firebaseDBHelper?.getUserData(firebaseUser, object : UserDetailsCallback {
            override fun onUserDataReceived(user: ModelUser) {
                modelUser?.value = user
            }

            override fun onUserNotExists() {
                modelUser?.value = ModelUser()
            }

            override fun onFailed(error: String) {
                isErrorOccurred?.value = Pair(true,"Failed To Get Data From Cloud")
            }
        })
    }

    fun authenticateUserUsingFirebase(account : GoogleSignInAccount?) {
        isLoading?.value=(Pair(true,"Authenticating..."))
        account?.idToken?.let { googleSingInHelper?.firebaseAuthWithGoogle(it) }
    }

    fun getUserData() : LiveData<ModelUser>? {
        return modelUser
    }

    fun getIsLoading() : LiveData<Pair<Boolean, String>>? {
        return isLoading
    }

    fun getHasErrorOccurred() : LiveData<Pair<Boolean, String>>? {
        return isErrorOccurred
    }

}