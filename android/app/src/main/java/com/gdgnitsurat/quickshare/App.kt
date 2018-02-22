package com.gdgnitsurat.quickshare

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }


    companion object {
        private var instance: App? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}
