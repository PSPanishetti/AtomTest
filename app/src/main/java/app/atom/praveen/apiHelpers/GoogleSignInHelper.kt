package app.atom.praveen.apiHelpers

import android.util.Log
import app.atom.praveen.interfaces.FirebaseAuthCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class GoogleSignInHelper(val firebaseAuthCallback: FirebaseAuthCallback) {

    private val TAG = "GoogleSignInHelper"
    private val mAuth = FirebaseAuth.getInstance()

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        firebaseAuthCallback.onSuccess(mAuth.currentUser)
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        firebaseAuthCallback.onFailed(task.exception.toString())
                    }
                }
            })

    }

}