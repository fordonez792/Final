package com.example.afinal

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class LoginViewModel: ViewModel() {
    private val db = Firebase.firestore
    val collectionRef = db.collection("images")

    enum class AuthenticateState {
        AUNTHENTICATED, UNAUTHENTICATED
    }
    val authenticateState = LoginStateLiveData().map { user ->
        if(user!=null) {
            AuthenticateState.AUNTHENTICATED
        } else {
            AuthenticateState.UNAUTHENTICATED
        }
    }

    fun uploadMessage(imageUrl: String?, name: String?, description: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user!!.email
        val index = email?.indexOf('@')
        val username = email?.substring(0, index!!)
        val image = ImageFirestore(name, description, imageUrl, username, null)

        collectionRef.add(image)
            .addOnSuccessListener { docRef ->
                val uri = Uri.parse(image.imageUrl)
                val key = docRef.id
                val storageRef = Firebase.storage
                    .getReference(user!!.uid)
                    .child(key)
                    .child(uri.lastPathSegment!!)
                putImageInStorage(storageRef, uri, docRef)
            }
            .addOnFailureListener { e ->
                Log.d("LoginViewModel", "Error while adding the image")
            }
    }

    private fun putImageInStorage(storageRef: StorageReference, uri: Uri, docRef: DocumentReference) {
        storageRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        val photoUrl = url.toString()
                        docRef.update("imageUrl", photoUrl)
                    }
            }
            .addOnFailureListener { e ->
                Log.d("LoginViewModel", "Error when writing image to storage")
            }
    }
}