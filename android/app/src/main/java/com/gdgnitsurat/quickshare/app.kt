package com.gdgnitsurat.quickshare

import android.app.Application

import com.google.firebase.FirebaseApp

/**
 * Created by yolo on 5/1/18.
 */

class app : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
