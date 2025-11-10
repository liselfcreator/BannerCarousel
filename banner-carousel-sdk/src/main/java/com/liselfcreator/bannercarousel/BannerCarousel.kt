package com.liselfcreator.bannercarousel

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 自定义 Banner 轮播视图，内部封装 ViewPager2 + 动画效果。
 */
class BannerCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewPager2: ViewPager2 = ViewPager2(context)
    private var pageChangeCallback: OnPageChangeCallback? = null
    private var currentConfig: Config? = null

    init {
        clipChildren = false
        clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false
        viewPager2.overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        addView(viewPager2, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    /**
     * 通过 Builder 配置轮播。
     */
    fun setup(block: Builder.() -> Unit) {
        val config = Builder().apply(block).build(context)
        applyConfig(config)
    }

    private fun applyConfig(config: Config) {
        currentConfig = config
        val screenWidth = ScreenUtils.getScreenWidth(context)

        val spacingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            config.itemSpacingDp.toFloat(),
            resources.displayMetrics
        ).roundToInt()

        val denominator = (config.edgeVisibleFraction * 2f) + 2f + config.centerScaleFactor
        require(denominator > 0f) { "Invalid combination of edgeVisibleFraction and centerScaleFactor." }

        val availableWidth = (screenWidth - spacingPx * 4).coerceAtLeast(1)
        val normalWidthFloat = availableWidth.toFloat() / denominator
        val normalWidth = normalWidthFloat.roundToInt().coerceAtLeast(1)

        val normalHeight = (normalWidthFloat / config.aspectRatio.width * config.aspectRatio.height).roundToInt()
            .coerceAtLeast(1)
        val centerWidthFloat = normalWidthFloat * config.centerScaleFactor
        val centerHeight = (centerWidthFloat / config.aspectRatio.width * config.aspectRatio.height).roundToInt()
            .coerceAtLeast(normalHeight)

        val heightDiff = (centerHeight - normalHeight).coerceAtLeast(0)
        val paddingTop: Int
        val paddingBottom: Int
        if (config.scalePivot == ScalePivotType.TOP) {
            paddingTop = heightDiff
            paddingBottom = 0
        } else {
            val halfPadding = (heightDiff / 2f).roundToInt()
            paddingTop = halfPadding
            paddingBottom = (heightDiff - halfPadding).coerceAtLeast(0)
        }
        val horizontalPadding = ((screenWidth - normalWidth).toFloat() / 2f).roundToInt().coerceAtLeast(0)

        viewPager2.offscreenPageLimit = 2
        viewPager2.adapter = BannerCarouselAdapter(
            data = config.data,
            showStatus = config.showStatus,
            showBottom = config.showBottom,
            onItemClickListener = config.onItemClickListener,
            imageLoader = config.imageLoader
        )

        val recyclerView = viewPager2.getChildAt(0) as RecyclerView
        recyclerView.apply {
            clipToPadding = false
            clipChildren = false
            isNestedScrollingEnabled = false
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPadding(horizontalPadding, paddingTop, horizontalPadding, paddingBottom)
            layoutParams = layoutParams?.apply { height = centerHeight }
        }

        viewPager2.layoutParams = viewPager2.layoutParams?.apply {
            height = centerHeight
        } ?: LayoutParams(LayoutParams.MATCH_PARENT, centerHeight)

        layoutParams = layoutParams?.apply {
            height = centerHeight
        } ?: LayoutParams(LayoutParams.MATCH_PARENT, centerHeight)

        val compositeTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(spacingPx))
            addTransformer(
                ScaleInTransformer(
                    minScaleX = 1.0f,
                    minScaleY = 1.0f,
                    centerScaleX = config.centerScaleFactor,
                    centerScaleY = config.centerScaleFactor,
                    scalePivotType = config.scalePivot
                )
            )
        }
        viewPager2.setPageTransformer(compositeTransformer)

        registerPageChangeCallback(config)

        viewPager2.setCurrentItem(BannerCarouselAdapter.getOriginalPosition(config.data.size), false)
        notifyInitialPage(config)
    }

    private fun registerPageChangeCallback(config: Config) {
        pageChangeCallback?.let { viewPager2.unregisterOnPageChangeCallback(it) }
        val listener = config.onPageChangeListener ?: return
        val callback = object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val realIndex = resolveRealPosition(position, config.data.size)
                config.data.getOrNull(realIndex)?.let { model ->
                    listener.invoke(realIndex, model)
                }
            }
        }
        pageChangeCallback = callback
        viewPager2.registerOnPageChangeCallback(callback)
    }

    private fun notifyInitialPage(config: Config) {
        config.onPageChangeListener ?: return
        val initial = resolveRealPosition(viewPager2.currentItem, config.data.size)
        config.data.getOrNull(initial)?.let { model ->
            config.onPageChangeListener.invoke(initial, model)
        }
    }

    private fun resolveRealPosition(position: Int, pageSize: Int): Int {
        if (pageSize == 0) return 0
        val mod = position % pageSize
        return if (mod < 0) mod + pageSize else mod
    }

    override fun onDetachedFromWindow() {
        pageChangeCallback?.let { viewPager2.unregisterOnPageChangeCallback(it) }
        pageChangeCallback = null
        super.onDetachedFromWindow()
    }

    data class AspectRatio(val width: Int, val height: Int)

    data class Config(
        val data: List<BannerModel>,
        val showStatus: Boolean,
        val showBottom: Boolean,
        val itemSpacingDp: Int,
        val edgeVisibleFraction: Float,
        val centerScaleFactor: Float,
        val aspectRatio: AspectRatio,
        val scalePivot: ScalePivotType,
        val imageLoader: BannerImageLoader,
        val onItemClickListener: ((Int, BannerModel) -> Unit)?,
        val onPageChangeListener: ((Int, BannerModel) -> Unit)?
    )

    class Builder internal constructor() {
        private var data: List<BannerModel> = emptyList()
        private var showStatus: Boolean = true
        private var showBottom: Boolean = true
        private var itemSpacingDp: Int = 17
        private var edgeVisibleFraction: Float = 0.2f
        private var centerScaleFactor: Float = 1.8f
        private var aspectRatio: String = "3:4"
        private var scalePivot: ScalePivotType = ScalePivotType.CENTER
        private var imageLoader: BannerImageLoader? = null
        private var onItemClickListener: ((Int, BannerModel) -> Unit)? = null
        private var onPageChangeListener: ((Int, BannerModel) -> Unit)? = null

        fun setData(data: List<BannerModel>) = apply { this.data = data }
        fun showStatus(show: Boolean) = apply { this.showStatus = show }
        fun showBottom(show: Boolean) = apply { this.showBottom = show }
        fun setItemSpacingDp(spacingDp: Int) = apply { this.itemSpacingDp = max(spacingDp, 0) }
        fun setEdgeVisibleFraction(fraction: Float) = apply {
            this.edgeVisibleFraction = fraction.coerceIn(0f, 1f)
        }
        fun setCenterScaleFactor(scale: Float) = apply {
            this.centerScaleFactor = max(scale, 1f)
        }
        fun setItemAspectRatio(ratio: String) = apply { this.aspectRatio = ratio }
        fun setScalePivot(pivot: ScalePivotType) = apply { this.scalePivot = pivot }
        fun setImageLoader(loader: BannerImageLoader) = apply { this.imageLoader = loader }
        fun setOnItemClickListener(listener: ((Int, BannerModel) -> Unit)?) = apply {
            this.onItemClickListener = listener
        }
        fun setOnPageChangeListener(listener: ((Int, BannerModel) -> Unit)?) = apply {
            this.onPageChangeListener = listener
        }

        internal fun build(context: Context): Config {
            require(data.isNotEmpty()) { "Banner data list cannot be empty." }
            val aspect = parseAspectRatio(aspectRatio)
            val loader = imageLoader ?: BannerImageLoader { view, _, placeholder ->
                view.setImageResource(placeholder)
            }
            return Config(
                data = data,
                showStatus = showStatus,
                showBottom = showBottom,
                itemSpacingDp = itemSpacingDp,
                edgeVisibleFraction = edgeVisibleFraction,
                centerScaleFactor = centerScaleFactor,
                aspectRatio = aspect,
                scalePivot = scalePivot,
                imageLoader = loader,
                onItemClickListener = onItemClickListener,
                onPageChangeListener = onPageChangeListener
            )
        }

        private fun parseAspectRatio(ratio: String): AspectRatio {
            val sanitized = ratio.replace(" ", "")
            val delimiter = when {
                sanitized.contains(":") -> ":"
                sanitized.contains("/") -> "/"
                else -> null
            }
            val parts = delimiter?.let { sanitized.split(it) } ?: listOf(sanitized, "0")
            val width = parts.getOrNull(0)?.toIntOrNull()?.coerceAtLeast(1) ?: 3
            val height = parts.getOrNull(1)?.toIntOrNull()?.coerceAtLeast(1) ?: 4
            return AspectRatio(width, height)
        }
    }
}

