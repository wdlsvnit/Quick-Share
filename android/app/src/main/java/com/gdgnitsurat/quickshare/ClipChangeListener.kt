package com.gdgnitsurat.quickshare

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Gravity
import android.view.WindowManager


/**
 * Created by yolo on 13/1/18.
 */
class ClipChangeListener : ClipboardManager.OnPrimaryClipChangedListener {


    var mLocalService: service
    var mLocalUtil: popupUtil

    constructor(service: service) {
        this.mLocalService = service
        this.mLocalService.mLocalUtil = popupUtil(mLocalService)
        this.mLocalUtil = mLocalService.mLocalUtil
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPrimaryClipChanged() {
        Log.e("ClipChangeListener", "Primary Clip Changed")
        var clip: String
        do {
            var mLocalClipData: ClipData
            mLocalClipData = this.mLocalService.mClipboardManager.primaryClip
            clip = mLocalClipData.getItemAt(0).coerceToText(this.mLocalService.applicationContext).toString()
            Log.e("ClipChangeListener", "Clip: " + clip)
        } while (!this.mLocalService.clipDescription(clip))
        showPopupView()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun showPopupView() {
        val mLocalLayoutParams: WindowManager.LayoutParams = mLocalUtil.mLayoutParams
        mLocalLayoutParams.gravity = Gravity.TOP; Gravity.LEFT
        mLocalUtil.mWindowManager = mLocalUtil.mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLocalLayoutParams.x = mLocalUtil.mWindowManager.defaultDisplay.width
        mLocalLayoutParams.y = 550
        if (mLocalUtil.mInitialPopupView != null && !mLocalUtil.mInitialPopupView.isAttachedToWindow) {
            mLocalUtil.mWindowManager.addView(mLocalUtil.mInitialPopupView, mLocalLayoutParams)
            mLocalUtil.mInitialPopupView.startAnimation()
        }
        Log.e("ClipChangedListener", "PopupViewShowed")
    }

//    fun showPopupView(){
//
//        val windowManager2 = mLocalUtil.mContext.getSystemService(WINDOW_SERVICE) as WindowManager
//        val layoutInflater = mLocalUtil.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = layoutInflater.inflate(R.layout.initial_popup_view_layout, null)
//        val params :WindowManager.LayoutParams
//        params = WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT)
//
//        params.gravity = Gravity.CENTER or Gravity.CENTER
//        params.x = 0
//        params.y = 0
//        windowManager2.addView(view,params)
//    }

}