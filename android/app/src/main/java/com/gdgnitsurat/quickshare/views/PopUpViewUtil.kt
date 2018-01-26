package com.gdgnitsurat.quickshare.views

import android.content.Context
import android.graphics.PixelFormat
import android.view.WindowManager

class PopUpViewUtil(var context: Context) {

    val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
    )
    var initialPopupView: InitialPopupView
    var windowManager: WindowManager

    init {
        this.initialPopupView = InitialPopupView(this.context, layoutParams)
        this.windowManager = this.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}