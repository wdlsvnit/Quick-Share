package com.gdgnitsurat.quickshare.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.gdgnitsurat.quickshare.activities.MainActivity
import com.gdgnitsurat.quickshare.utils.FirebaseUtil
import com.gdgnitsurat.quickshare.views.PopUpViewUtil
import java.util.*
import kotlin.concurrent.timer

class ClipChangeListener(ClipboardService: ClipboardService) : ClipboardManager.OnPrimaryClipChangedListener {

    private var clipboardService: ClipboardService = ClipboardService
    private var popUpViewUtil: PopUpViewUtil

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPrimaryClipChanged() {
        Log.e("ClipChangeListener", "Primary Clip Changed")
        var clip: String
        do {
            val mLocalClipData: ClipData = this.clipboardService.clipboardManager.primaryClip
            clip = mLocalClipData.getItemAt(0).coerceToText(this.clipboardService.applicationContext).toString()
            var currentTime :Date = Calendar.getInstance().time
            Log.e("ClipChangeListener", "Clip: " + clip)
            FirebaseUtil.addClipToFirebaseDatabase(clip,currentTime.toString())
        } while (!this.clipboardService.clipDescription(clip))
        showPopupView()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showPopupView() {
        val mLocalLayoutParams: WindowManager.LayoutParams = popUpViewUtil.layoutParams
        popUpViewUtil.windowManager = popUpViewUtil.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLocalLayoutParams.gravity = Gravity.TOP; Gravity.START
        mLocalLayoutParams.x = popUpViewUtil.windowManager.defaultDisplay.width
        mLocalLayoutParams.y = 550
        if (popUpViewUtil.initialPopupView != null && !popUpViewUtil.initialPopupView.isAttachedToWindow) {
            popUpViewUtil.windowManager.addView(popUpViewUtil.initialPopupView, mLocalLayoutParams)
            popUpViewUtil.initialPopupView.startAnimation()
        }
        Log.e("ClipChangedListener", "PopupViewShowed")
    }

    init {
        this.clipboardService.popUpViewUtil = PopUpViewUtil(clipboardService)
        this.popUpViewUtil = clipboardService.popUpViewUtil
    }
}