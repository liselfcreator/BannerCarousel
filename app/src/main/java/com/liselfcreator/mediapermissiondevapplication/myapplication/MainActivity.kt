package com.liselfcreator.mediapermissiondevapplication.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.liselfcreator.bannercarousel.BannerCarouselView
import com.liselfcreator.bannercarousel.BannerImageLoader
import com.liselfcreator.bannercarousel.BannerModel

class MainActivity : AppCompatActivity() {
    private lateinit var bannerCarouselView: BannerCarouselView
    private lateinit var previewImage: ImageView
    private val bannerList: List<BannerModel> = Data.getData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        bannerCarouselView = findViewById(R.id.banner_carousel)
        previewImage = findViewById(R.id.ImgView)

        bannerCarouselView.setup {
            setData(bannerList)
            showStatus(false)
            showBottom(false)
            setItemSpacingDp(5)
            setEdgeVisibleFraction(0.6f)
            setItemAspectRatio("9:16")
            setCenterScaleFactor(2f)
            setImageLoader(BannerImageLoader { imageView, url, placeholder ->
                Glide.with(imageView)
                    .load(url)
                    .placeholder(placeholder)
                    .into(imageView)
            })
            setOnItemClickListener { _, _ ->
                // 示例：点击事件打印或做跳转
            }
            setOnPageChangeListener { _, model ->
                updatePreview(model)
            }
        }
    }

    private fun updatePreview(model: BannerModel) {
        Glide.with(this)
            .load(model.imgUrl)
            .placeholder(R.drawable.ic_default_icon_placeholder)
            .into(previewImage)
    }
}