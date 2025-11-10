package com.liselfcreator.bannercarousel

import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

internal class ScaleInTransformer(
    private var minScaleX: Float = 0.675f,
    private var minScaleY: Float = minScaleX,
    private var centerScaleX: Float = minScaleX * 1.5f,
    private var centerScaleY: Float = minScaleY * 1.5f
) : ViewPager2.PageTransformer {

    constructor(minScale: Float) : this(minScale, minScale, minScale * 1.5f, minScale * 1.5f)

    constructor(minScaleX: Float, minScaleY: Float) : this(minScaleX, minScaleY, minScaleX * 1.5f, minScaleY * 1.5f)

    override fun transformPage(view: View, position: Float) {
        val absPosition = abs(position)

        val (scaleX, scaleY) = when {
            absPosition <= 1f -> {
                val factor = 1.0f - absPosition
                val interpolatedScaleX = minScaleX + (centerScaleX - minScaleX) * factor
                val interpolatedScaleY = minScaleY + (centerScaleY - minScaleY) * factor
                interpolatedScaleX to interpolatedScaleY
            }
            else -> minScaleX to minScaleY
        }

        val pageWidth = view.width.toFloat()
        val pageHeight = view.height.toFloat()
        val banner = view.findViewById<View>(R.id.img_banner)
        val badge = view.findViewById<View>(R.id.image_book_completed)
        val bottom = view.findViewById<View>(R.id.bottom_layout)

        view.pivotX = pageWidth * 0.5f
        view.pivotY = pageHeight * 0.5f
        view.scaleX = 1f
        view.scaleY = 1f

        var deltaX = 0f
        var deltaY = 0f
        banner?.let {
            val width = it.width
            val height = it.height
            if (width != 0 && height != 0) {
                it.pivotX = width * 0.5f
                it.pivotY = height * 0.5f
                deltaX = (scaleX - 1f) * width / 2f
                deltaY = (scaleY - 1f) * height / 2f
            }
            it.scaleX = scaleX
            it.scaleY = scaleY
        }

        badge?.let {
            it.pivotX = 0f
            it.pivotY = 0f
            it.scaleX = 1f
            it.scaleY = 1f
            it.translationX = -deltaX
            it.translationY = -deltaY
        }

        bottom?.let {
            val width = it.width
            val height = it.height
            if (width != 0 && height != 0) {
                it.pivotX = width * 0.5f
                it.pivotY = height.toFloat()
            }
            it.scaleX = scaleX
            it.scaleY = 1f
            it.translationX = 0f
            it.translationY = deltaY

            val viewGroup = it as? ViewGroup
            viewGroup?.let { group ->
                val inverseScaleX = if (scaleX != 0f) 1f / scaleX else 1f
                for (i in 0 until group.childCount) {
                    val child = group.getChildAt(i)
                    child.pivotX = child.width * 0.5f
                    child.pivotY = child.height * 0.5f
                    child.scaleX = inverseScaleX
                    child.scaleY = 1f
                }
            }
        }

        val baseTranslationX = view.translationX
        val extraOffset = if (centerScaleX > minScaleX) {
            val maxOffset = ((centerScaleX - minScaleX) * pageWidth) / 2f
            val cappedProgress = absPosition.coerceAtMost(1f)
            val direction = when {
                position > 0f -> 1f
                position < 0f -> -1f
                else -> 0f
            }
            direction * maxOffset * cappedProgress
        } else {
            0f
        }
        view.translationX = baseTranslationX + extraOffset
        view.translationY = 0f

        view.translationZ = (1f - absPosition).coerceAtLeast(0f)
    }
}

