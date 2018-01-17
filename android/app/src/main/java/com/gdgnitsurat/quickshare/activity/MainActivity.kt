package com.gdgnitsurat.quickshare.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.service
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
        Log.e("sdvsdbv", "Start Service")
        startService(Intent(this, service::class.java))

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
            menu?.findItem(R.id.menu_item_sign_out)?.isVisible = false
            textView.text = "Sign in to get started"
        } else {
            signInButton.visibility = View.INVISIBLE
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
//                    AuthUI.getInstance()
//                            .signOut(this)
//                            .addOnCompleteListener {
//                                Log.e("Auth : ", "Sign out successful")
//                                updateUI()
//                            }
                    Log.e("sdvsdbv", "Stopping Service")
                    stopService(Intent(this, service::class.java))
                }
            }
        }

        return false
    }
}
