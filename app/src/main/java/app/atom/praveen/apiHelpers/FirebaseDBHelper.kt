package app.atom.praveen.apiHelpers

import android.util.Log
import app.atom.praveen.interfaces.UserDetailsCallback
import app.atom.praveen.interfaces.UserUpdateCallback
import app.atom.praveen.models.ModelUser
import app.atom.praveen.others.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseDBHelper {

    val db = FirebaseDatabase.getInstance()
    val userBranchReference = db.getReference(Constants.USER_BRANCH_REFERENCE)

  public fun getUserData(user: FirebaseUser?, userDetailsCallback: UserDetailsCallback){

      user?.uid?.let { userBranchReference.child(it).addListenerForSingleValueEvent(object :ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              Log.d("FirebaseDBHelper"," onDataChange : $snapshot")
             val user = snapshot.getValue(ModelUser::class.java)
             if (user != null) {
                  userDetailsCallback.onUserDataReceived(user)
              }else{
                 Log.d("FirebaseDBHelper","User Does't Exist onDataChange : $snapshot")
                 userDetailsCallback.onUserNotExists()
             }
          }

          override fun onCancelled(error: DatabaseError) {
                  userDetailsCallback.onFailed(error.toString())
          }
      }) }
  }

    public fun updaterUserData(firebaseUser: FirebaseUser,user: ModelUser, callback : UserUpdateCallback){
        userBranchReference.child(firebaseUser.uid).setValue(user).addOnSuccessListener {
            callback.onSuccess(user)
        }.addOnFailureListener {
            callback.onFailed(it.toString())
        }

    }

}