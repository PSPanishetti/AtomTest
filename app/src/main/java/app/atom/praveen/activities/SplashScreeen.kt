package app.atom.praveen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import app.atom.praveen.R
import app.atom.praveen.others.Constants
import app.atom.praveen.others.SharedPrefsManager

/**
 * Have't followed any patterns for this
 */

class SplashScreeen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screeen)

        Handler(Looper.getMainLooper()).postDelayed({
           if(SharedPrefsManager(this).getBooleanValue(Constants.IS_USER_SIGNED_IN,false))
           {
               startActivity(Intent(this,Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
               finish()
           }else{
               startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
               finish()
           }
        }, 5000)
    }
}