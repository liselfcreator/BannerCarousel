package com.liselfcreator.bannercarousel

/**
 * SDK 对外暴露的 Banner 数据模型。
 *
 * @property imgUrl  封面图地址
 * @property status  完结状态；用于决定顶部徽标是已完结还是连载中
 * @property score   评分文案（可选）
 * @property views   阅读/观看人数文案（可选）
 */
data class BannerModel(
    val imgUrl: String,
    val status: BannerStatus,
    val score: String? = null,
    val views: String? = null
)

/**
 * 完结状态枚举。
 */
enum class BannerStatus {
    COMPLETED,
    ONGOING
}

