package com.gdgnitsurat.quickshare.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.model.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.net.NetworkInterface
import java.util.*


class AuthUIActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

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

//        Log.e("MAC ADDRESS  ",getMACAddress("wlan0"))
//        Log.e("MAC ADDRESS 1 ",getMACAddress("eth0"))
//        Log.e("MAC ADDRESS 2 ",get())
    }

    private fun signIn() {
        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        )

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()
                , RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
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
        val user = user(firebaseAuth.currentUser?.displayName, firebaseAuth.currentUser?.email)
        databaseRef.child("users").child(firebaseAuth.currentUser?.uid).setValue(user)
    }

//    private fun getMACAddress(interfaceName :String):String{
//        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
//        for (intf : NetworkInterface in interfaces){
//            if (interfaceName != null){
//                if (intf.name.equals(interfaceName,true)) continue
//            }
//            var mac = intf.hardwareAddress;
//            if (mac==null) return ""
//            var buf = StringBuilder()
//            for (i in mac.indices){
//                buf.append(String.format("%02X:",mac[i]))
//            }
//            if (buf.length>0){
//                buf.deleteCharAt(buf.length-1)
//            }
//            return buf.toString()
//        }
//        return ""
//    }


//    @SuppressLint("WifiManagerLeak")
//    private fun get():String{
//        val wifimanager :WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
//        val wifiInfo = wifimanager.connectionInfo
//        return wifiInfo.macAddress
//    }

}
