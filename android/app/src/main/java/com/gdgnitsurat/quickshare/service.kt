package com.gdgnitsurat.quickshare

import android.app.Service
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

/**
 * Created by yolo on 11/1/18.
 */

class service : Service() {

    lateinit var mClipboardManager: ClipboardManager
    lateinit var mClipChangeListener: ClipboardManager.OnPrimaryClipChangedListener
    lateinit var mLocalUtil: popupUtil

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//       return START_STICKY
//    }

    override fun onCreate() {
        super.onCreate()
        this.mClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        this.mClipChangeListener = ClipChangeListener(this)
        this.mClipboardManager.addPrimaryClipChangedListener(this.mClipChangeListener)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mClipboardManager.removePrimaryClipChangedListener(this.mClipChangeListener)
        Toast.makeText(this, "service done", Toast.LENGTH_LONG).show()
    }

    fun clipDescription(clip: String): Boolean {
        val mLocalClipDescription: ClipDescription? = this.mClipboardManager.primaryClipDescription
        if (mLocalClipDescription == null) {
            return false
        }
        if ((!mLocalClipDescription.hasMimeType("text/plain")) && (!mLocalClipDescription.hasMimeType("text/html"))) {
            return false
        }
        return true
    }

}
