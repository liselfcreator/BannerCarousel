package com.liselfcreator.mediapermissiondevapplication.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * 自定义 FrameLayout，强制保持 3:4 的宽高比例
 * 用于 ViewPager2，确保每个 item 都是精确的 3:4 比例
 * 内部的图片使用 centerCrop 会自动从中间裁剪
 */
class RatioFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 宽高比例：宽/高 = 3/4 = 0.75
    private var aspectRatio: Float = 3f / 4f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取父容器给的宽度
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        
        // 根据宽度计算精确的 3:4 比例高度
        // 为了避免精度问题，确保宽度是 3 的倍数
        val adjustedWidth = (width / 3) * 3  // 调整为 3 的倍数
        val height = adjustedWidth * 4 / 3   // 精确计算高度（整数运算）
        
        // 创建新的 MeasureSpec
        val newWidthSpec = View.MeasureSpec.makeMeasureSpec(adjustedWidth, View.MeasureSpec.EXACTLY)
        val newHeightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        
        // 使用调整后的尺寸测量子 View
        super.onMeasure(newWidthSpec, newHeightSpec)
        
        // 设置最终尺寸
        setMeasuredDimension(adjustedWidth, height)
    }
}

