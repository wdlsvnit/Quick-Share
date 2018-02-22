package com.gdgnitsurat.quickshare.utils

import android.content.Context
import com.gdgnitsurat.quickshare.App
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
    private var deviceID = ""

    init {
        getDeviceID()
    }

    fun addCurrentUserToFirebaseDatabase() {
        val databaseRef: DatabaseReference = database.getReference("users")
        val user = User(firebaseAuth.currentUser?.displayName, firebaseAuth.currentUser?.email)
        databaseRef.child(firebaseAuth.currentUser?.uid).setValue(user)
    }

    fun addDeviceToFirebaseDatabase() {
        val databaseRef: DatabaseReference = database.reference
        val device = Device("android", firebaseAuth.currentUser?.uid)
        databaseRef.child("devices").child(deviceID).setValue(device)
        databaseRef.child("users").child(firebaseAuth.currentUser?.uid).child("devices").setValue(deviceID)
    }

    fun addClipToFirebaseDatabase(s: String, time: String) {
        val databaseRef1: DatabaseReference = database.getReference("devices").child(deviceID).child("clips")
        val databaseRef: DatabaseReference = database.getReference("clips").push()
        val clip = Clip(s, time, firebaseAuth.currentUser?.uid, deviceID)
        var key = databaseRef.key
        databaseRef.setValue(clip)
        databaseRef1.child(key).setValue(true)
    }

    fun getDeviceID() {
        val sharedPreference = App.applicationContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        deviceID = sharedPreference.getString(Constants.DEVICE_PREF, "")
    }
}