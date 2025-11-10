package com.liselfcreator.bannercarousel

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

internal object ScreenUtils {
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    @Suppress("DEPRECATION")
    fun getScreenFullHeight(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.currentWindowMetrics.bounds.height()
        } else {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}

