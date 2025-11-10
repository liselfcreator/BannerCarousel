package com.liselfcreator.bannercarousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.roundToInt

/**
 * 对外暴露的 SDK 入口，负责接管 ViewPager2 并应用轮播动画。
 */
class BannerCarousel private constructor(
    private val viewPager2: ViewPager2,
    private val data: List<BannerModel>,
    private val showStatus: Boolean,
    private val showBottom: Boolean,
    private val onItemClickListener: ((position: Int, model: BannerModel) -> Unit)?
) {

    fun attach() {
        require(data.isNotEmpty()) { "BannerCarousel requires at least one BannerModel." }

        val context = viewPager2.context
        val screenWidth = ScreenUtils.getScreenWidth(context)
        val spacing = (17f * context.resources.displayMetrics.density).roundToInt()
        val totalSpacing = spacing * 4
        val xFloat = (screenWidth - totalSpacing).toFloat() / 4.2f
        val xRaw = xFloat.roundToInt()
        val x = (xRaw / 3) * 3

        val normalWidth = x
        val enlargedWidth = ((x * 1.8f).roundToInt() / 3) * 3
        val normalHeight = normalWidth * 4 / 3
        val enlargedHeight = enlargedWidth * 4 / 3

        val containerHeight = enlargedHeight
        val verticalPadding = ((containerHeight - normalHeight) / 2f).roundToInt().coerceAtLeast(0)
        val horizontalPadding = (screenWidth - normalWidth) / 2

        viewPager2.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2

            val adapter = BannerCarouselAdapter(
                data = data,
                showStatus = showStatus,
                showBottom = showBottom,
                onItemClickListener = onItemClickListener
            )
            this.adapter = adapter
            setCurrentItem(BannerCarouselAdapter.getOriginalPosition(data.size), false)

            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                isNestedScrollingEnabled = false
                clipToPadding = false
                clipChildren = false
                setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
                layoutParams = layoutParams?.apply { height = containerHeight }
            }

            layoutParams = layoutParams?.apply { height = containerHeight }

            val compositeTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(spacing))
                addTransformer(ScaleInTransformer(1.0f, 1.0f, 1.8f, 1.8f))
            }
            setPageTransformer(compositeTransformer)
        }
    }

    class Builder(private val viewPager2: ViewPager2) {
        private var data: List<BannerModel> = emptyList()
        private var showStatus: Boolean = true
        private var showBottom: Boolean = true
        private var onItemClickListener: ((position: Int, model: BannerModel) -> Unit)? = null

        fun setData(data: List<BannerModel>) = apply { this.data = data }

        fun showStatus(show: Boolean) = apply { this.showStatus = show }

        fun showBottom(show: Boolean) = apply { this.showBottom = show }

        fun setOnItemClickListener(listener: ((position: Int, model: BannerModel) -> Unit)?) = apply {
            this.onItemClickListener = listener
        }

        fun build(): BannerCarousel {
            viewPager2.visibility = View.VISIBLE
            return BannerCarousel(
                viewPager2 = viewPager2,
                data = data,
                showStatus = showStatus,
                showBottom = showBottom,
                onItemClickListener = onItemClickListener
            ).also { it.attach() }
        }
    }

    companion object {
        @JvmStatic
        fun with(viewPager2: ViewPager2): Builder = Builder(viewPager2)
    }
}

