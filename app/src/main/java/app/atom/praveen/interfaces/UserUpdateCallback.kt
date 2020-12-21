package app.atom.praveen.interfaces

import app.atom.praveen.models.ModelUser

interface UserUpdateCallback {
    fun  onSuccess(user : ModelUser)
    fun onFailed(error :String)
}