package com.gdgnitsurat.quickshare

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.initial_popup_view_layout.view.*

/**
 * Created by yolo on 13/1/18.
 */
class InitialPopupView(context: Context?) : FrameLayout(context) {

    lateinit var mLayoutParams: WindowManager.LayoutParams
    lateinit var mWindowmanger: WindowManager
    lateinit var mAnimationPopupIconContainer: Animation
    lateinit var mAnimationPopupIconGlow: Animation
    lateinit var mWindowManager: WindowManager

    //    val mInitialPopupView = initial_popup_view
    lateinit var mInitialPopupIconContainer: View
    lateinit var mIntialPopupGlow: View

    constructor(mContext: Context, mLayoutParams: WindowManager.LayoutParams) : this(mContext) {
        mWindowmanger = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.mLayoutParams = mLayoutParams
        LayoutInflater.from(context).inflate(R.layout.initial_popup_view_layout, this, true)
        this.mInitialPopupIconContainer = initial_popup_icon_container
        this.mIntialPopupGlow = initial_popup_glow
        val mAccessibilityManager: AccessibilityManager = mContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (mAccessibilityManager.isTouchExplorationEnabled) {
            this.mInitialPopupIconContainer.isClickable = true
            this.mInitialPopupIconContainer.setOnClickListener {
                Toast.makeText(mContext, "IconContainerClicked", Toast.LENGTH_LONG).show()
                Log.e("InitialPopupView", "PopupViewIconContainerClicked")
            }
            return
        }
        val initialPopupViewRef: InitialPopupView = this
        mAnimationPopupIconContainer = AnimationUtils.loadAnimation(mContext, R.anim.initial_popup_icon_container)
        mAnimationPopupIconGlow = AnimationUtils.loadAnimation(mContext, R.anim.intial_popup_icon_glow)
        mAnimationPopupIconContainer.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                mWindowManager.removeView(initialPopupViewRef)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }

    fun startAnimation() {
        this.mInitialPopupIconContainer.startAnimation(mAnimationPopupIconContainer)
        this.mIntialPopupGlow.startAnimation(mAnimationPopupIconGlow)
    }


}


