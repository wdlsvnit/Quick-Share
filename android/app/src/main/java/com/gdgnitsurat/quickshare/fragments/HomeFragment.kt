package com.gdgnitsurat.quickshare.fragments

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gdgnitsurat.quickshare.R
import com.gdgnitsurat.quickshare.clipboard.ClipboardService
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        serviceSwitch.isChecked = isClipBoardServiceRunning(ClipboardService::class.java)
        serviceSwitch.setOnCheckedChangeListener({ _, isChecked ->
            run {
                if (isChecked) {
                    startClipboardService()
                } else {
                    stopClipboardService()
                }
            }
        })
    }

    private fun startClipboardService() {
        activity?.startService(Intent(activity, ClipboardService::class.java))
        Log.e("ClipboardService", "Clipboard Service started")
    }

    private fun stopClipboardService() {
        activity?.stopService(Intent(activity, ClipboardService::class.java))
        Log.e("ClipboardService", "Clipboard Service stopped")
    }

    private fun isClipBoardServiceRunning(serviceClass: Class<ClipboardService>): Boolean {
        val activityManager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }
}
