@file:Suppress("UsePropertyAccessSyntax", "UsePropertyAccessSyntax", "UsePropertyAccessSyntax")

package app.atom.praveen.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.atom.praveen.databinding.ActivityRegistrationBinding
import app.atom.praveen.dialogs.SimpleLoadingDialog
import app.atom.praveen.models.ModelUser
import app.atom.praveen.others.Constants
import app.atom.praveen.viewModels.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationViewModel:RegistrationViewModel
    private  var isGuestModeActive : Boolean=false
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var simpleLoadingDialog: SimpleLoadingDialog
    private val TAG="RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTextWatcherOnName()

        if(intent.hasExtra(Constants.IS_GUEST_MODE_ACTIVE)){
            isGuestModeActive=intent.getBooleanExtra(Constants.IS_GUEST_MODE_ACTIVE,false)
        }

        if(intent.hasExtra(Constants.INTENT_DISPLAY_NAME)){
            binding.editTextName.setText(intent.getStringExtra(Constants.INTENT_DISPLAY_NAME))
            binding.continueFab.isEnabled = true
            binding.editTextName.setSelection(binding.editTextName.getText().toString().length)
        }

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        registrationViewModel.initViewModel()

        simpleLoadingDialog = SimpleLoadingDialog(this)

        registrationViewModel.getUserModel()?.observe(this,{
            if(it!=null){
                moveToHomeActivity()
            }
        })

        registrationViewModel.getIsLoading()?.observe(this,{
            if(it.first)
            {
                showLoading(it.second)
            }else{
                hideLoading()
            }
        })

        registrationViewModel.getIsErrorOccurred()?.observe(this,{
            if(it.first)
            { hideLoading()
                toastError(it.second)
            }

        })

        binding.continueFab.setOnClickListener {
            saveUserInfo()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

    }


    private fun toastError(error: String) {
        Toast.makeText(
            this,
            "Something Went Wrong \nError : $error\n Please Try Again Later",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveUserInfo() {
        if(isGuestModeActive) {
            moveToHomeActivity()
        }else{
            val names = binding.editTextName.getText().toString()
            if(firebaseUser!=null) {
                showLoading("Updating Profile....")
                registrationViewModel.uploadUserData(ModelUser(displayName = names,userId = firebaseUser.uid, isProfileUpdated = true))
            }
        }
    }

    private fun moveToHomeActivity() {
        hideLoading()
        startActivity(Intent(this, Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun showLoading(message: String) {
        Log.d(TAG,"ShowLoading : $message")
        simpleLoadingDialog.show()
        simpleLoadingDialog.setMessage(message)
    }

    private fun hideLoading() {
        if(simpleLoadingDialog!=null){
            simpleLoadingDialog.setCancelable(true)
            simpleLoadingDialog.dismiss()
        }
    }


    private fun setTextWatcherOnName() {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.continueFab.isEnabled = binding.editTextName.getText().toString().isNotEmpty()

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}