package com.gdgnitsurat.quickshare.clipboard

import android.app.Service
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.gdgnitsurat.quickshare.views.PopUpViewUtil

class ClipboardService : Service() {

    lateinit var clipboardManager: ClipboardManager
    lateinit var clipChangeListener: ClipboardManager.OnPrimaryClipChangedListener
    lateinit var popUpViewUtil: PopUpViewUtil

    override fun onCreate() {
        super.onCreate()
        this.clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        this.clipChangeListener = ClipChangeListener(this)
        this.clipboardManager.addPrimaryClipChangedListener(this.clipChangeListener)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clipboardManager.removePrimaryClipChangedListener(this.clipChangeListener)
        Toast.makeText(this, "Clipboard Service destroyed", Toast.LENGTH_LONG).show()
    }

    fun clipDescription(clip: String): Boolean {
        val mLocalClipDescription: ClipDescription = this.clipboardManager.primaryClipDescription
                ?: return false

        if ((!mLocalClipDescription.hasMimeType("text/plain")) && (!mLocalClipDescription.hasMimeType("text/html"))) {
            return false
        }
        return true
    }
}
