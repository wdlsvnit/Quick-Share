package com.gdgnitsurat.quickshare.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.model.User
import com.gdgnitsurat.quickshare.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class AuthUIActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_ui)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            return
        }

        signIn()
    }

    private fun signIn() {
        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        )

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()
                , Constants.REQUEST_CODE_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CODE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                showToast(getString(R.string.sign_in_successful))
                addUserToFirebaseDatabase()
            } else {
                // Sign in failed

                if (response == null) {
                    // User pressed back button
                    showToast(getString(R.string.sign_in_cancelled))
                }

                when (response?.errorCode) {
                    ErrorCodes.NO_NETWORK -> showToast(getString(R.string.no_internet_connection))
                    ErrorCodes.UNKNOWN_ERROR -> showToast(getString(R.string.unknown_error))
                    else -> showToast(getString(R.string.unknown_response))
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun addUserToFirebaseDatabase() {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef: DatabaseReference = database.reference
        val user = User(firebaseAuth.currentUser?.displayName, firebaseAuth.currentUser?.email)
        databaseRef.child("users").child(firebaseAuth.currentUser?.uid).setValue(user)
    }
}
