package app.atom.praveen.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.atom.praveen.R
import app.atom.praveen.databinding.ActivityLoginBinding
import app.atom.praveen.dialogs.SimpleLoadingDialog
import app.atom.praveen.models.ModelUser
import app.atom.praveen.others.Constants
import app.atom.praveen.viewModels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() , View.OnClickListener{


    private lateinit var loginViewModel: LoginViewModel
    private val TAG="LoginActivity"

    private val RC_SIGN_IN: Int = 5050
    private lateinit var binding : ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var simpleLoadingDialog: SimpleLoadingDialog
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueAsGuest.setOnClickListener(this)
        binding.continueWithGoogle.setOnClickListener(this)

        setupGoogleSignInClient()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.initializeModel()

        simpleLoadingDialog=  SimpleLoadingDialog(this)

        loginViewModel.getUserData()?.observe(this, {
            updateUI(it)
        })

        loginViewModel.getIsLoading()?.observe(this,{
            Log.d(TAG,"We got something here " )
            if(it.first) {
                showLoading(it.second)
            }else{
                hideLoading()
            }
        })

        loginViewModel.getHasErrorOccurred()?.observe(this,{
            if(it.first)
            {
                hideLoading()
                toastError(it.second)
            }
        })

        loginViewModel

    }

    private fun toastError(message: String) {
        Toast.makeText(this, "Something Went Wrong\nError : $message\n\nPlease Try Again Later", Toast.LENGTH_LONG).show()
    }

    private fun hideLoading() {
        simpleLoadingDialog.setCancelable(true)
        simpleLoadingDialog.dismiss()
    }

    private fun updateUI(user: ModelUser?) {

        hideLoading()

        if(user!=null){
            firebaseUser= FirebaseAuth.getInstance().currentUser!!
            if(user.isProfileUpdated){
                startActivity(Intent(this,Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }else{
                startActivity(Intent(this,RegistrationActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Constants.INTENT_DISPLAY_NAME,firebaseUser.displayName))
                finish()
            }
        }
    }

    private fun setupGoogleSignInClient() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_api_key))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.continue_as_guest -> moveToUserRegistration()
            R.id.continue_with_google -> prepareForGoogleOauth()
        }
    }

    private fun prepareForGoogleOauth() {
        showLoading("Getting Google Accounts...")
        performGoogleSignIn()
    }

    private fun showLoading(message: String) {
        Log.d(TAG,"ShowLoading : $message")
        simpleLoadingDialog.show()
        simpleLoadingDialog.setMessage(message)

    }

    private fun moveToUserRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java).putExtra(Constants.IS_GUEST_MODE_ACTIVE,true))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            loginViewModel.authenticateUserUsingFirebase(account)
        } catch (e: ApiException) {
            simpleLoadingDialog.dismiss()
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun performGoogleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}