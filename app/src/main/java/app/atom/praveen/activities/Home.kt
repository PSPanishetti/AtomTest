package app.atom.praveen.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.atom.praveen.databinding.ActivityHomeBinding
import app.atom.praveen.others.Constants
import app.atom.praveen.others.SharedPrefsManager


/**
 * Have't followed any patterns for this
 */

class Home : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        saveDataToPrefs()

        binding.logout.setOnClickListener {
            SharedPrefsManager(this).saveBooleanValue(Constants.IS_USER_SIGNED_IN, false)
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }

        binding.goToRepo.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PSPanishetti/AtomTest"))
            startActivity(browserIntent)
        }


    }

    /**
     * Just keeping things Simple
     */
    private fun saveDataToPrefs() {
        SharedPrefsManager(this).saveBooleanValue(Constants.IS_USER_SIGNED_IN, true)
    }
}