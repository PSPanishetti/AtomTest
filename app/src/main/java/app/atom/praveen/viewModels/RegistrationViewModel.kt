package app.atom.praveen.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.atom.praveen.apiHelpers.FirebaseDBHelper
import app.atom.praveen.interfaces.UserUpdateCallback
import app.atom.praveen.models.ModelUser
import com.google.firebase.auth.FirebaseAuth

class RegistrationViewModel : ViewModel() {

    private var modelUser: MutableLiveData<ModelUser>? = null
    private var isLoading : MutableLiveData<Pair<Boolean, String>> ? =null
    private var isErrorOccurred : MutableLiveData<Pair<Boolean, String>> ? = null
    private var firebaseUser = FirebaseAuth.getInstance().currentUser
    private val firebaseDbHelper= FirebaseDBHelper

    fun initViewModel(){
        if(modelUser==null){
            modelUser= MutableLiveData<ModelUser>()
            isLoading = MutableLiveData<Pair<Boolean,String>>()
            isErrorOccurred = MutableLiveData()
        }
    }

    fun uploadUserData(user : ModelUser){
        firebaseUser?.let {
            firebaseDbHelper.updaterUserData(it,user,object :UserUpdateCallback{
                override fun onSuccess(user: ModelUser) {
                    modelUser?.postValue(user)
                }

                override fun onFailed(error: String) {
                    isErrorOccurred?.postValue(Pair(true,error))
                }
            })
        }
    }

    fun getUserModel() : LiveData<ModelUser>?{
        return modelUser
    }

    fun getIsLoading() : LiveData<Pair<Boolean,String>>?{
        return isLoading
    }


    fun getIsErrorOccurred() : LiveData<Pair<Boolean,String>>?{
        return isErrorOccurred
    }

}