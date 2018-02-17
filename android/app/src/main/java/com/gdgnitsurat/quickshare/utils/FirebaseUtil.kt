package com.gdgnitsurat.quickshare.utils

import android.provider.Settings
import android.util.Log
import com.gdgnitsurat.quickshare.activities.MainActivity
import com.gdgnitsurat.quickshare.model.Clip
import com.gdgnitsurat.quickshare.model.Device
import com.gdgnitsurat.quickshare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var deviceid = ""


    fun addCurrentUserToFirebaseDatabase() {
        val databaseRef: DatabaseReference = database.getReference("users")
        val user = User(firebaseAuth.currentUser?.displayName, firebaseAuth.currentUser?.email)
        databaseRef.child(firebaseAuth.currentUser?.uid).setValue(user)
    }

    fun addDeviceToFirebaseDatabase(deviceID:String) {
        deviceid = deviceID
        val databaseRef: DatabaseReference = database.reference
        val device = Device("android", firebaseAuth.currentUser?.uid)
        databaseRef.child("devices").child(deviceID).setValue(device)
        databaseRef.child("users").child(firebaseAuth.currentUser?.uid).child("devices").setValue(deviceID)
    }

    fun addClipToFirebaseDatabase(clip:String,time:String) {
        val id = deviceid
        val databaseRef1: DatabaseReference = database.getReference("devices").child(id).child("clips")
        val databaseRef: DatabaseReference = database.getReference("clips").push()
        val clip = Clip(clip,time, firebaseAuth.currentUser?.uid, deviceid)
        var key = databaseRef.key
        databaseRef.setValue(clip)
        databaseRef1.child(key).setValue(true)
    }

    fun isUserSignedIn(): Boolean {
        firebaseUser = firebaseAuth.currentUser
        return firebaseUser != null
    }

}