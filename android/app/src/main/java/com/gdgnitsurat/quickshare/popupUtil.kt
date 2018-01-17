package com.gdgnitsurat.quickshare

import android.content.Context
import android.graphics.PixelFormat
import android.view.WindowManager

/**
 * Created by yolo on 13/1/18.
 */
class popupUtil {
    val mLayoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
    )
    var mContext: Context
    var mInitialPopupView: InitialPopupView
    lateinit var mWindowManager: WindowManager

    constructor(context: Context) {
        this.mContext = context
        this.mInitialPopupView = InitialPopupView(mContext, mLayoutParams)
    }

}