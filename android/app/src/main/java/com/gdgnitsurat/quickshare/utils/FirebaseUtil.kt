package com.gdgnitsurat.quickshare.utils

import com.gdgnitsurat.quickshare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    fun addCurrentUserToFirebaseDatabase() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef: DatabaseReference = database.reference
        val user = User(firebaseAuth.currentUser?.displayName, firebaseAuth.currentUser?.email)
        databaseRef.child("users").child(firebaseAuth.currentUser?.uid).setValue(user)
    }

    fun isUserSignedIn(): Boolean {
        firebaseUser = firebaseAuth.currentUser
        return firebaseUser != null
    }
}