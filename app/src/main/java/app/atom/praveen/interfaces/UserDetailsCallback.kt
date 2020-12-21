package app.atom.praveen.interfaces

import app.atom.praveen.models.ModelUser

interface UserDetailsCallback {
    fun onUserDataReceived(user : ModelUser)
    fun onUserNotExists()
    fun onFailed(error :String)
}