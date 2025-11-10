package com.liselfcreator.mediapermissiondevapplication.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.liselfcreator.bannercarousel.BannerCarousel
import com.liselfcreator.bannercarousel.BannerModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var previewImage: ImageView
    private val bannerList: List<BannerModel> = Data.getData()

    private val pageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updatePreview(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager2Container = findViewById<ViewPager2Container>(R.id.view_pager2_container)
        viewPager2Container?.clipToPadding = false
        viewPager2Container?.clipChildren = false

        viewPager2 = findViewById(R.id.view_pager2)
        previewImage = findViewById(R.id.ImgView)

        BannerCarousel.with(viewPager2)
            .setData(bannerList)
            .showStatus(true)
            .showBottom(true)
            .setOnItemClickListener { position, model ->
                // 示例：点击事件打印或做跳转
                // Toast.makeText(this, "Click: ${model.imgUrl}", Toast.LENGTH_SHORT).show()
            }
            .build()

        viewPager2.registerOnPageChangeCallback(pageChangeCallback)

        if (bannerList.isNotEmpty()) {
            val original = calculateOriginalPosition(bannerList.size)
            updatePreview(original)
        }
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updatePreview(position: Int) {
        val realPosition = resolveRealPosition(position, bannerList.size)
        val model = bannerList.getOrNull(realPosition) ?: return
        Glide.with(this)
            .load(model.imgUrl)
            .placeholder(R.drawable.ic_default_icon_placeholder)
            .into(previewImage)
    }

    private fun calculateOriginalPosition(pageSize: Int): Int {
        if (pageSize == 0) return 0
        val mid = Int.MAX_VALUE / 2
        return mid - (mid % pageSize)
    }

    private fun resolveRealPosition(position: Int, pageSize: Int): Int {
        if (pageSize == 0) return 0
        val mod = position % pageSize
        return if (mod < 0) mod + pageSize else mod
    }
}