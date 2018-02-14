package com.gdgnitsurat.quickshare.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.fragments.DevicesFragment
import com.gdgnitsurat.quickshare.fragments.HomeFragment
import com.gdgnitsurat.quickshare.utils.Constants
import com.gdgnitsurat.quickshare.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private var drawer: Drawer? = null
    private var header: AccountHeader? = null
    private var profileDrawerItem: ProfileDrawerItem? = null
    private var itemSignIn: PrimaryDrawerItem? = null
    private var itemSignOut: PrimaryDrawerItem? = null
    private var itemVerifiedProfile: PrimaryDrawerItem? = null
    private var itemUnverifiedProfile: PrimaryDrawerItem? = null
    private var itemHome: PrimaryDrawerItem? = null
    private var itemDevices: PrimaryDrawerItem? = null
    private var itemSettings: PrimaryDrawerItem? = null
    private var currentProfile: PrimaryDrawerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        instantiateUser()
        instantiateMenuItems()
        setupProfileDrawer()
        setupNavigationDrawerWithHeader()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun instantiateUser() {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    private fun checkCurrentProfileStatus(): PrimaryDrawerItem {
        currentProfile = if (firebaseAuth.currentUser?.isEmailVerified!!) {
            itemVerifiedProfile
        } else {
            itemUnverifiedProfile
        }
        return currentProfile as PrimaryDrawerItem
    }

    private fun instantiateMenuItems() {
        itemVerifiedProfile = PrimaryDrawerItem().withIdentifier(Constants.ITEM_VERIFIED_PROFILE.toLong()).withName(R.string.verified_profile).withIcon(resources.getDrawable(R.drawable.ic_verified_user_black_24px)).withSelectable(false)
        itemUnverifiedProfile = PrimaryDrawerItem().withIdentifier(Constants.ITEM_UNVERIFIED_PROFILE.toLong()).withName(R.string.unverified_profile).withIcon(resources.getDrawable(R.drawable.ic_report_problem_black_24px))

        itemSignIn = PrimaryDrawerItem().withIdentifier(Constants.ITEM_SIGN_IN.toLong()).withName(R.string.menu_item_sign_in).withIcon(resources.getDrawable(R.drawable.ic_account_circle_black_24px)).withSelectable(false)
        itemSignOut = PrimaryDrawerItem().withIdentifier(Constants.ITEM_SIGN_OUT.toLong()).withName(R.string.menu_item_sign_out).withIcon(resources.getDrawable(R.drawable.ic_sign_out)).withSelectable(false)

        itemHome = PrimaryDrawerItem().withIdentifier(Constants.ITEM_HOME.toLong()).withName(R.string.menu_item_home).withIcon(resources.getDrawable(R.drawable.ic_home_black_24dp))
        itemDevices = PrimaryDrawerItem().withIdentifier(Constants.ITEM_DEVICES.toLong()).withName(R.string.menu_item_devices).withIcon(resources.getDrawable(R.drawable.ic_devices_black_24dp))
        itemSettings = PrimaryDrawerItem().withIdentifier(Constants.ITEM_SETTINGS.toLong()).withName(R.string.menu_item_settings).withIcon(resources.getDrawable(R.drawable.ic_settings_black_24dp)).withSelectable(false)
    }

    private fun setupProfileDrawer() {
        profileDrawerItem = if (isUserSignedIn()) {
            ProfileDrawerItem()
                    .withName(firebaseAuth.currentUser?.displayName)
                    .withEmail(firebaseAuth.currentUser?.email)
                    .withIcon(resources.getDrawable(R.drawable.ic_account_circle_black_24px))
        } else {
            ProfileDrawerItem()
                    .withIcon(resources.getDrawable(R.drawable.ic_account_circle_black_24px))
        }
    }

    private fun setupAccountHeader(): AccountHeader {
        header = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profileDrawerItem)
                .withOnAccountHeaderListener { view, profile, currentProfile -> false }
                .withSelectionListEnabledForSingleProfile(false)
                .build()
        return header as AccountHeader
    }

    private fun setupNavigationDrawerWithHeader() {
        if (!isUserSignedIn()) {
            drawer = DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(setupAccountHeader())
                    .withToolbar(toolbar)
                    .addDrawerItems(itemSignIn, DividerDrawerItem(), itemHome, itemDevices, itemSettings, DividerDrawerItem())
                    .withOnDrawerItemClickListener { _, _, drawerItem ->
                        onNavDrawerItemSelected(drawerItem.identifier.toInt())
                        true
                    }
                    .build()
        } else {
            currentProfile = checkCurrentProfileStatus()
            drawer = DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(setupAccountHeader())
                    .withToolbar(toolbar)
                    .addDrawerItems(currentProfile, DividerDrawerItem(), itemHome, itemDevices, itemSettings, DividerDrawerItem(), itemSignOut)
                    .withOnDrawerItemClickListener { view, position, drawerItem ->
                        onNavDrawerItemSelected(drawerItem.identifier.toInt())
                        true
                    }
                    .build()
        }

        onNavDrawerItemSelected(Constants.ITEM_HOME)
        drawer?.setSelection(itemHome?.identifier!!)
        (drawer as Drawer).closeDrawer()
    }

    private fun onNavDrawerItemSelected(drawerItemIdentifier: Int) {
        when (drawerItemIdentifier) {
            Constants.ITEM_UNVERIFIED_PROFILE -> {
                //Send verification mail
            }
            Constants.ITEM_SIGN_IN -> {
                startAuthUIActivity()
            }
            Constants.ITEM_HOME -> {
                replaceFragment(HomeFragment())
            }
            Constants.ITEM_DEVICES -> {
                replaceFragment(DevicesFragment())
            }
            Constants.ITEM_SETTINGS -> {
                //Start settings activity
            }
            Constants.ITEM_SIGN_OUT -> {
                signOutUser()
            }
        }

        if (drawer?.isDrawerOpen!!)
            drawer?.closeDrawer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST_CODE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                //Successfully signed in
                Toast.makeText(this, R.string.sign_in_success, Toast.LENGTH_LONG).show()
                updateUIAfterSignIn()
                FirebaseUtil.addCurrentUserToFirebaseDatabase()
                return
            } else {
                //User pressed back button
                if (response == null) {
                    Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_LONG).show()
                    drawer?.deselect(itemSignIn?.identifier!!)
                    return
                }

                //No internet connection.
                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, R.string.no_connectivity, Toast.LENGTH_LONG).show()
                    return
                }

                //Unknown error
                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, R.string.sign_in_unknown_Error, Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
    }

    private fun refreshMenuHeader() {
        drawer?.closeDrawer()
        header?.clear()
        setupProfileDrawer()
        setupAccountHeader()
        drawer?.header = header?.view!!
        drawer?.resetDrawerContent()
    }

    private fun updateUIAfterSignIn() {
        instantiateUser()
        if (!firebaseAuth.currentUser?.isEmailVerified!!) {
            //mFirebaseUser.sendEmailVerification();
        }
        currentProfile = checkCurrentProfileStatus()
        drawer?.updateItemAtPosition(currentProfile!!, 1)
        itemSignOut?.let { drawer?.addItemAtPosition(it, Constants.ITEM_SIGN_OUT) }
        drawer?.deselect(itemSignOut?.identifier!!)
        refreshMenuHeader()
    }

    private fun signOutUser() {
        //Sign out
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    Toast.makeText(this, "Signed out successfully ", Toast.LENGTH_SHORT).show()
                    Log.e("Auth : ", "Signed out successfully")

                    if (!isUserSignedIn()) {
                        itemSignIn?.let { drawer?.updateItemAtPosition(it, 1) }
                        drawer?.removeItem(itemSignOut?.identifier!!)
                        drawer?.deselect(itemSignIn?.identifier!!)
                        refreshMenuHeader()
                    } else {
                        //check if internet connectivity is there
                        Toast.makeText(this, "Sign out failed ", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun startAuthUIActivity() {
        val providers: List<AuthUI.IdpConfig> = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        )

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.initial_popup_logo)
                .setAllowNewEmailAccounts(true)
                .setIsSmartLockEnabled(false)
                .build(), Constants.REQUEST_CODE_SIGN_IN)
    }
}
