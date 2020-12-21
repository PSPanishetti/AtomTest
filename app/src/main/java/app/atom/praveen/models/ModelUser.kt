package app.atom.praveen.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ModelUser(var userId:String?,var displayName:String?, var isProfileUpdated: Boolean){
  constructor() : this(null,null,false)
}