package com.beemer.unofficial.fromis_9.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics

object UiUtils {
    // dp -> px 단위 변환
    fun dpToPx(context: Context, dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    // Glide Placeholder
    fun createPlaceholderDrawable(context: Context, width: Int, height: Int): BitmapDrawable {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        return BitmapDrawable(context.resources, bitmap)
    }
}