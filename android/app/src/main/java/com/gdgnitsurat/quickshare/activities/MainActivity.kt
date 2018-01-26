package com.gdgnitsurat.quickshare.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.clipboard.ClipboardService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        signInButton.setOnClickListener { invalidateSignIn() }

        serviceSwitch.isChecked = isClipBoardServiceRunning(ClipboardService::class.java)
        serviceSwitch.setOnCheckedChangeListener({ _, isChecked ->
            run {
                if (isChecked) {
                    startClipboardService()
                } else {
                    stopClipboardService()
                }
            }
        })
    }

    private fun startClipboardService() {
        startService(Intent(application, ClipboardService::class.java))
        Log.e("ClipboardService", "Clipboard Service started")
    }

    private fun stopClipboardService() {
        stopService(Intent(application, ClipboardService::class.java))
        Log.e("ClipboardService", "Clipboard Service stopped")
    }

    private fun isClipBoardServiceRunning(serviceClass: Class<ClipboardService>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }

    private fun invalidateSignIn() {
        updateUI()
        if (!isUserSignedIn()) {
            startActivity(Intent(this, AuthUIActivity::class.java))
        }
    }

    private fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private fun updateUI() {
        if (!isUserSignedIn()) {
            signInButton.visibility = View.VISIBLE
            serviceSwitch.visibility = View.INVISIBLE
            menu?.findItem(R.id.menu_item_sign_out)?.isVisible = false
            textView.text = getString(R.string.sign_in_to_get_started)
        } else {
            signInButton.visibility = View.INVISIBLE
            serviceSwitch.visibility = View.VISIBLE
            menu?.findItem(R.id.menu_item_sign_out)?.isVisible = true
            textView.text = firebaseAuth.currentUser?.email
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu, menu)
        updateUI()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_sign_out -> {
                if (isUserSignedIn()) {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener {
                                Log.e("Auth : ", "Sign out successful")
                                updateUI()
                            }
                    stopClipboardService()
                }
            }
        }
        return false
    }
}
