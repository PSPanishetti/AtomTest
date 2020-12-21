package app.atom.praveen.interfaces

import app.atom.praveen.models.ModelUser
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthCallback {
    fun onSuccess(firebaseUser: FirebaseUser?)
    fun onFailed(error : String)
}