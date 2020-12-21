package app.atom.praveen.others

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(val context: Context,
                         private val sharedPreference: SharedPreferences=context.getSharedPreferences(Constants.SHARED_PREFS_FILE_NAME,Context.MODE_PRIVATE),
                         private @SuppressLint("CommitPrefEdits") val editor : SharedPreferences.Editor= sharedPreference.edit())
{

    fun saveBooleanValue(key:String,value: Boolean){
        editor.putBoolean(key,value)
        editor.commit()
        editor.apply()
    }

    fun getBooleanValue(key:String,defaultVal: Boolean):Boolean{
       return sharedPreference.getBoolean(key,defaultVal)
    }

}
