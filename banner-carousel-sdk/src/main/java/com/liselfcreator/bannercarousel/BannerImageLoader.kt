package com.liselfcreator.bannercarousel

import android.widget.ImageView

/**
 * 外部图片加载抽象，方便宿主应用接入任意图片框架。
 *
 * @param target 目标 ImageView
 * @param url    图片地址，可能为空
 * @param placeholderResId 默认占位图资源
 */
fun interface BannerImageLoader {
    fun load(target: ImageView, url: String?, placeholderResId: Int)
}

