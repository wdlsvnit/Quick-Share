package com.gdgnitsurat.quickshare.views

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
import com.gdgnitsurat.quickshare.R
import kotlinx.android.synthetic.main.initial_popup_view_layout.view.*

class InitialPopupView(context: Context?) : FrameLayout(context) {

    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager

    private lateinit var animationPopupIconContainer: Animation
    private lateinit var animationPopupIconGlow: Animation

    private lateinit var initialPopupIconContainer: View
    private lateinit var initialPopupGlow: View

    constructor(mContext: Context, mLayoutParams: WindowManager.LayoutParams) : this(mContext) {
        windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.layoutParams = mLayoutParams
        LayoutInflater.from(context).inflate(R.layout.initial_popup_view_layout, this, true)
        this.initialPopupIconContainer = initial_popup_icon_container
        this.initialPopupGlow = initial_popup_glow
        val mAccessibilityManager: AccessibilityManager = mContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        if (mAccessibilityManager.isTouchExplorationEnabled) {
            this.initialPopupIconContainer.isClickable = true
            this.initialPopupIconContainer.setOnClickListener {
                Toast.makeText(mContext, "IconContainerClicked", Toast.LENGTH_LONG).show()
                Log.e("InitialPopupView", "PopupViewIconContainerClicked")
            }
            return
        }

        val initialPopupViewRef: InitialPopupView = this
        animationPopupIconContainer = AnimationUtils.loadAnimation(mContext, R.anim.initial_popup_icon_container)
        animationPopupIconGlow = AnimationUtils.loadAnimation(mContext, R.anim.intial_popup_icon_glow)
        animationPopupIconContainer.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                windowManager.removeView(initialPopupViewRef)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    fun startAnimation() {
        this.initialPopupIconContainer.startAnimation(animationPopupIconContainer)
        this.initialPopupGlow.startAnimation(animationPopupIconGlow)
    }
}


